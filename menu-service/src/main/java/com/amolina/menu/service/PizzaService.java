package com.amolina.menu.service;

import com.amolina.menu.model.Pizza;
import com.amolina.menu.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PizzaService {

    @Autowired
    private PizzaRepository pizzaRepository;

    public List<Pizza> getAllPizzas() {
        return pizzaRepository.findAll();
    }

    public Optional<Pizza> getPizzaById(Long id) {
        return pizzaRepository.findById(id);
    }

    public Pizza createPizza(Pizza pizza) {
        return pizzaRepository.save(pizza);
    }

    public Pizza updatePizza(Long id, Pizza pizzaDetails) {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pizza not found with id: " + id));
        
        pizza.setName(pizzaDetails.getName());
        pizza.setDescription(pizzaDetails.getDescription());
        pizza.setPrice(pizzaDetails.getPrice());
        
        return pizzaRepository.save(pizza);
    }

    public void deletePizza(Long id) {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pizza not found with id: " + id));
        pizzaRepository.delete(pizza);
    }
}

