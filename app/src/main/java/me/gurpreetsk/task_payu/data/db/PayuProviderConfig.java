package me.gurpreetsk.task_payu.data.db;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

@SimpleSQLConfig(
        name = "PayuProvider",
        authority = "me.gurpreetsk.task_payu",
        database = "payu.db",
        version = 1)
public class PayuProviderConfig implements ProviderConfig {

    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }

}
