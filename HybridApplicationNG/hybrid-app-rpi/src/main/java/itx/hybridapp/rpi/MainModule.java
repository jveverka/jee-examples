package itx.hybridapp.rpi;

import com.google.inject.AbstractModule;

import itx.hybridapp.rpi.services.config.ConfigService;
import itx.hybridapp.rpi.services.config.ConfigServiceImpl;

public class MainModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConfigService.class).to(ConfigServiceImpl.class);
	}

}
