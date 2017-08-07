package org.itnaf.metadata.transformation;

import java.util.HashMap;

public class UserNameTransformation implements Transformation {

	@Override
	public String transformation(HashMap<String, String> parameters) {
		return parameters.get("default") + parameters.get(" transformed");
	}

}
