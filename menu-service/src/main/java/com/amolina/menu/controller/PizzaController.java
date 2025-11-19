package com.amolina.menu.controller;

import com.amolina.menu.model.Pizza;
import com.amolina.menu.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {

    @Autowired
    private PizzaService pizzaService;

    @GetMapping
    public ResponseEntity<List<Pizza>> getAllPizzas() {
        List<Pizza> pizzas = pizzaService.getAllPizzas();
        return ResponseEntity.ok(pizzas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pizza> getPizzaById(@PathVariable Long id) {
        return pizzaService.getPizzaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RolesAllowed("ADMIN")
    @PostMapping
    public ResponseEntity<Pizza> createPizza(@RequestBody Pizza pizza) {
        Pizza createdPizza = pizzaService.createPizza(pizza);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPizza);
    }

    @RolesAllowed("ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<Pizza> updatePizza(@PathVariable Long id, @RequestBody Pizza pizza) {
        try {
            Pizza updatedPizza = pizzaService.updatePizza(id, pizza);
            return ResponseEntity.ok(updatedPizza);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RolesAllowed("ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePizza(@PathVariable Long id) {
        try {
            pizzaService.deletePizza(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

