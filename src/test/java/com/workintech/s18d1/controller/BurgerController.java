package com.workintech.s18d1.controller;

import com.workintech.s18d1.dao.BurgerDao;
import com.workintech.s18d1.entity.BreadType;
import com.workintech.s18d1.entity.Burger;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workintech/burgers")
@Slf4j
@AllArgsConstructor
public class BurgerController {
    private final BurgerDao burgerDao;

    @GetMapping
    public List<Burger> findAll() {
        return burgerDao.findAll();
    }

   @GetMapping("/{id}")
    public Burger find(@PathVariable("id") Integer id) {
        return burgerDao.findById(id);
   }

   @PostMapping
    public Burger save(@Valid @RequestBody Burger burger) {
        return burgerDao.save(burger);
   }

   @PutMapping
    public Burger update(@Valid @RequestBody Burger burger) {
        return burgerDao.update(burger);
   }

   @DeleteMapping("/{id}")
    public Burger remove(@PathVariable("id") Integer id ) {
        return burgerDao.remove(id);
   }

   @GetMapping("/findByPrice")
    public List<Burger> findByPrice(@RequestBody Double price) {
        return burgerDao.findByPrice(price);
   }

   @GetMapping("/findByBreadType")
    public List<Burger> findByBreadType(@RequestBody BreadType breadType) {
        return burgerDao.findByBreadType(breadType);
   }

   @GetMapping("/findByContent")
    public List<Burger> findByContent(@RequestBody String content) {
        return burgerDao.findByContent(content);
   }

}

