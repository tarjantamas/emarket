package com.tim32.emarket.util;

import android.content.Context;
import android.os.Build;
import android.util.Pair;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class DataValidatingToaster {

    private final Context context;

    private final List<Pair<Supplier<Boolean>, String>> rules = new ArrayList<>();

    public DataValidatingToaster(Context context) {
        this.context = context;
    }

    public DataValidatingToaster rule(Supplier<Boolean> rule, String errorMessage) {
        rules.add(Pair.create(rule, errorMessage));
        return this;
    }

    public boolean validateAllRules() {
        int currentRuleIndex = 0;
        try {
            while (currentRuleIndex < rules.size()) {
                Pair<Supplier<Boolean>, String> rule = rules.get(currentRuleIndex);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (!rule.first.get()) {
                        Toast.makeText(context, rule.second, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                currentRuleIndex++;
            }
        } catch (Exception e) {
            Pair<Supplier<Boolean>, String> rule = rules.get(currentRuleIndex);
            Toast.makeText(context, rule.second, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
