package com.cryptic.bingo.security;

import java.security.Key;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.SignatureAlgorithm;

public class SecurityConstants {
	public static final long EXPIRATION_TIME = 864000000;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/user";
	public static final String SIGN_UP_URL_REGISTERED = "/user/registered";
	public static final String SIGN_IN_URL = "/auth/login";
	public static final String[] ALLOWED_ORIGINS = { "http://localhost:32770", "http://crypticend.myds.me:32770" };

	public static byte[] getTokenSecret() {
		return Base64.getDecoder().decode(
				"renjigrebjirebgejirenjigrebjirebgejirenjigrebjirebgejirenjigrebjirebgejirenjigrebjirebgejirenjigrebjirebgejirenjigrebjirebgejirenjigrebjirebgeji");
	}

	public static Key getSigningKey() {
		return new SecretKeySpec(getTokenSecret(), SignatureAlgorithm.HS256.getJcaName());
	}
}
