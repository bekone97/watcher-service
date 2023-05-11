package com.miachyn.watcherservice.repository;

import com.miachyn.watcherservice.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Long> {
    Optional<Currency> findBySymbol(String symbol);
    boolean existsByIdAndSymbol(Long id, String symbol);
}
