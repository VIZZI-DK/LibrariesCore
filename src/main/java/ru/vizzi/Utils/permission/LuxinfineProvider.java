package ru.vizzi.Utils.permission;


import ml.luxinfine.config.api.ConfigAPI;
import ml.luxinfine.config.api.ConfigValue;
import ml.luxinfine.config.internal.LazyConfigValue;
import ml.luxinfine.helper.integrations.Economy;
import ml.luxinfine.helper.integrations.Permissions;
import ml.luxinfine.helper.integrations.Regions;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

@GradleSideOnly(GradleSide.SERVER)
@ml.luxinfine.config.api.Config(folder = "config/vizzi/")
public class LuxinfineProvider {

    @ConfigValue("Провайдер прав")
    public static LazyConfigValue<Permissions> permissions = ConfigAPI.lazy("dummy");

    @ConfigValue("Провайдер экономики")
    public static LazyConfigValue<Economy> economy = ConfigAPI.lazy("dummy");

    @ConfigValue("Провайдер регионов")
    public static LazyConfigValue<Regions> region = ConfigAPI.lazy("dummy");

}
