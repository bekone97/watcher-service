package com.miachyn.watcherservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyFollowerId implements Serializable {

    private Long user;

    private Long currency;

}
