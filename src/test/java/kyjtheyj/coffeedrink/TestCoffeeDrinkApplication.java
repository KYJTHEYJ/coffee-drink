package kyjtheyj.coffeedrink;

import org.springframework.boot.SpringApplication;

public class TestCoffeeDrinkApplication {

    public static void main(String[] args) {
        SpringApplication.from(CoffeeDrinkApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
