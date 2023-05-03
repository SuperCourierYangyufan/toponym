package com.example.toponym.utils;

import com.example.toponym.entity.constant.OtherInfoConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @author yyf
 */
@Slf4j
public class JwtUtil {

    /**
     * 生成token字符串的方法
     * @param id
     * @param username
     * @param claim
     * @return
     */
    public static String getJwtToken(String id, String username, Map<String, Object> claim) {
        return Jwts.builder()
                //JWT头信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS2256")
                //设置分类；设置过期时间 一个当前时间，一个加上设置的过期时间常量
                .setSubject(username)
                .setId(id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + OtherInfoConstant.JWT_OUT_TIME))
                //设置token主体信息，存储用户信息
                .addClaims(claim)
                //.signWith(SignatureAlgorithm.ES256, OtherInfoConstant.JWT_SECRET)
                .signWith(SignatureAlgorithm.HS256, OtherInfoConstant.JWT_SECRET)
                .compact();
    }

    /**
     * 判断token是否存在与有效
     * @Param jwtToken
     */
    public static boolean checkToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) {
            return false;
        }
        try {
            //验证token
            Jwts.parser().setSigningKey(OtherInfoConstant.JWT_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            log.error("JwtUtil.checkToken error,jwtToken:[{}]", jwtToken, e);
            return false;
        }
        return true;
    }


    /**
     * 解析token
     * @Param request
     */
    public static Claims parseToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(OtherInfoConstant.JWT_SECRET).parseClaimsJws(token);
        return claimsJws.getBody();
    }

}

