package com.workintech.s18d1.dao;

import com.workintech.s18d1.entity.BreadType;
import com.workintech.s18d1.entity.Burger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BurgerDaoImpl implements  BurgerDao{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Burger save(Burger burger) {
        entityManager.persist(burger);
        return burger;
    }

    @Override
    public Burger findById(Integer id) {
        return entityManager.find(Burger.class, id);
    }

    @Override
    public List<Burger> findAll() {
        return  entityManager.createQuery("SELECT b FROM Burger b", Burger.class ).getResultList();
    }

    @Override
    public List<Burger> findByPrice(double price) {
        return  entityManager.createQuery("SELECT b FROM Burger b WHERE b.price > :price ORDER BY b.price DESC",Burger.class)
                .setParameter("price", price)
                .getResultList();
    }

    @Override
    public List<Burger> findByBreadType(BreadType breadType) {
        return entityManager.createQuery("SELECT b FROM Burger b WHERE b.breadType = :breadType ORDER BY b.name ASC",Burger.class)
                .setParameter("breadType",breadType)
                .getResultList();
    }

    @Override
    public List<Burger> findByContent(String content) {
        return entityManager.createQuery("SELECT b FROM Burger b WHERE b.contents LIKE :content", Burger.class)
                .setParameter("content", "%" + content + "%")
                .getResultList();
    }

    @Override
    @Transactional
    public Burger update(Burger burger) {
        return entityManager.merge(burger);
    }

    @Override
    @Transactional
    public Burger remove(Integer id) {
        Burger burger = findById(id);
        entityManager.remove(burger);
        return burger;
    }
}
