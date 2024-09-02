package com.example.testingmyskills.Interfaces;

import java.util.Map;

public interface BalanceResponseCallback {
    void onBalanceReceived(Map<String, Object> response);

    void onLoadValues(Map<String, Object> response);
    void onLoadBalance(Map<String, Object> response);
}
