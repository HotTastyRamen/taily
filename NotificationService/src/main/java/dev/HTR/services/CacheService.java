package dev.HTR.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CacheService {

    @Autowired
    private final CacheManager cacheManager;

    private static final Random random = new Random();

    //Создать 4х код, закешировать

    @Cacheable(value = "ver_code_cache", key = "#userId")
    public Long createVerCode (Long userId){
        Long code = random.nextLong(1000, 10000);
        System.out.println(code);
        return code;
    }

    //Вывести при помощи кэша
    public Long getCache(Long userId) {
        Cache cache = cacheManager.getCache("ver_code_cache");
        Cache.ValueWrapper code;
        try {
            code = cache.get(userId);
            System.out.println(code.get());
            return (Long) code.get();
        } catch (ClassCastException e) {
            System.err.println("Ошибка приведения типа: " + e.getMessage());
            return null;
        }
    }
}
