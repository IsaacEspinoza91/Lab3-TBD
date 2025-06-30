package com.tbd.DeliveryMedicamentos.repositories;

import java.util.List;

public interface LogsPedidosRepositoryCustom {
    List<String> findPedidosConMasDe3CambiosEn10Min();
}
