package org.itnaf.metadata.transformation;

//import java.util.ArrayList;
import java.util.HashMap;

public class OUDUserName implements Transformation {

	@Override
	public String transformation(HashMap<String, String> parameters) {
		return parameters.get("") + parameters.get("");
	}

}
