package com.ftn.market.dto;

import java.util.HashMap;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class KeyValueResponse extends HashMap<String, Object> {

  public KeyValueResponse(final String key, final String value) {
    put(key, value);
  }
}
