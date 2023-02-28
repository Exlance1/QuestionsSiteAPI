package com.project.questapp.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component 
public class JwtTokenProvider {

	@Value("${questapp.app.secret}")
	private String APP_SECRET; //Özel bir key buna göre bir token oluşturacağız.
	
	@Value("${questapp.expires.in}")
	private long EXPIRES_IN; //Saniye cinsinden kaç saniyede token expire oluyor onun için kullanacağımız bir değişken.
	
	
	public String generateJwtToken(Authentication auth) {
		
		JwtUserDetails userDetails =(JwtUserDetails) auth.getPrincipal(); //Principal aslında authanticate edeceğimiz user demek.
		Date expireDate=new Date(new Date().getTime()+EXPIRES_IN); //Şuanki zamana expires_in'i ekleyip expireDate i buluyoruz 
		return Jwts.builder().setSubject(Long.toString(userDetails.getId()))
				.setIssuedAt(new Date()).setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512,APP_SECRET).compact();
	}
	
	//Keyden userId yi çıkaracağız bu methodda
	
	Long getUserIdFromJwt(String token){
		
		Claims claims=Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject()); //getSubject() string değer döndürdüğü için Long a pars ediyoruz.
	}
	
	//token ı oluşturduk şimdi validate etmemiz lazım.
	
	public boolean valideToken(String token){
		try {
			//Burada APP_SECRET ı kullanarak parse edebiliyorsak bu bizim uygulumamaızın oluşturduğu bir keydir.
			//Eğer öyle değilse catch bloğuna düşmüş olacak. Böylece validation işlemimizi gerçekleştirmiş oluyoruz.
			Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);	
			return !isTokenExpired(token);
		}
		catch(SignatureException e ){
			return false;
		}
		catch(MalformedJwtException e) {
			return false;
		}
		catch(ExpiredJwtException e) {
			return false;
		}
		catch(UnsupportedJwtException e) {
			return false;
		}
		catch(IllegalArgumentException e) {
			return false;
		}
		
	}

	private boolean isTokenExpired(String token) {
		Date expiration=Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody().getExpiration();
		return expiration.before(new Date());
	}
	
}
