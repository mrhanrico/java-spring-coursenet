package com.delivery.utilities;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.delivery.clients.GojekClient;
import com.delivery.clients.GrabClient;
import com.delivery.clients.ShipperClient;
import com.delivery.constants.ShipperIdConstant;

@Component
public class ShipperMapperUtility {
  @Autowired
  private GojekClient gojekClient;

  @Autowired
  private GrabClient grabClient;

  public ShipperClient mapShipperClient(Integer shipperId) {
    Map<Integer, ShipperClient> shipperMap = new HashMap<>();
    shipperMap.put(ShipperIdConstant.SHIPPER_ID_GOJEK, gojekClient);
    shipperMap.put(ShipperIdConstant.SHIPPER_ID_GRAB, grabClient);

    return shipperMap.get(shipperId);
  }
}
