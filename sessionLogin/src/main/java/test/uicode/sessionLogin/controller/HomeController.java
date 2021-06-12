package test.uicode.sessionLogin.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping
    public Object home(Authentication authentication) {
        return authentication.getPrincipal();
    }

    @PostMapping
    public String post() {
        return "POST";
    }

    @GetMapping("test")
    public String test() {
        return "test";
    }

    @GetMapping("admin")
    @Secured("ROLE_admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("redis")
    public Set<Object> getRedisKeys(@RequestParam String sessionKey) {
        return redisTemplate.boundHashOps(sessionKey).keys();
    }

    @GetMapping("redis/{key}")
    public Object getRedisValue(@RequestParam String sessionKey, @PathVariable String key) {
        return redisTemplate.boundHashOps(sessionKey).get(key);
    }

}
