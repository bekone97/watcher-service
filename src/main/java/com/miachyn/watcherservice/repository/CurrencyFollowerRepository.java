package com.miachyn.watcherservice.repository;

import com.miachyn.watcherservice.entity.CurrencyFollower;
import com.miachyn.watcherservice.entity.CurrencyFollowerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyFollowerRepository extends JpaRepository<CurrencyFollower, CurrencyFollowerId> {

    List<CurrencyFollower> findAllByCurrency_Id(Long currencyId);
    Optional<CurrencyFollower> findByCurrency_IdAndUser_Id(Long currencyId, Long userId);
}
