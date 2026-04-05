package com.workintech.s18d1;

import com.workintech.s18d1.entity.BreadType;
import com.workintech.s18d1.entity.Burger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {

    @Test
    void testBurgerSetAndGet() {
        Burger burger = new Burger();

        // 1L yerine 1 (Integer uyumu)
        burger.setId(1);
        burger.setName("Vegan Delight");
        burger.setPrice(8.99);

        // Lombok kuralı: isVegan değişkeni için setVegan metodunu kullanıyoruz
        burger.setVegan(true);

        burger.setBreadType(BreadType.WRAP);
        burger.setContents("Lettuce, Tomato, Vegan Patty, Avocado");

        // Doğrulamalar (Assertions)
        assertEquals(1, burger.getId());
        assertEquals("Vegan Delight", burger.getName());
        assertEquals(8.99, burger.getPrice());

        // Lombok kuralı: getIsVegan yerine isVegan() metodunu kullanıyoruz
        assertTrue(burger.isVegan());

        assertEquals(BreadType.WRAP, burger.getBreadType());
        assertEquals("Lettuce, Tomato, Vegan Patty, Avocado", burger.getContents());
    }

    @Test
    void testEnumValues() {
        // BreadType enum'ının 3 farklı tip içerdiğini kontrol ediyoruz
        assertEquals(3, BreadType.values().length);
    }
}