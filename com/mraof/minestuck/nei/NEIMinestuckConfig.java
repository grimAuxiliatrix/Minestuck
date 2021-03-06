package com.mraof.minestuck.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.asm.NEIModContainer;

import com.mraof.minestuck.util.Debug;

public class NEIMinestuckConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {
		API.registerRecipeHandler(new AlchemiterHandler());
		
		API.registerRecipeHandler(new DesignexHandler());
		API.registerUsageHandler(new DesignexHandler());
	}

	@Override
	public String getName() {
		return "Minestuck";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

}
