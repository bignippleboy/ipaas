/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */package com.github.ipaas.ifw.front.directive;

import java.io.IOException;
import java.util.Map;

import com.github.ipaas.ifw.front.FisException;
import com.github.ipaas.ifw.front.FisResource;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateScalarModel;

/**
 *
 * @author Chenql
 */
public class RequireDirective implements TemplateDirectiveModel {

	private FisResource fisRes;

	public RequireDirective(FisResource fisRes) {
		super();
		this.fisRes = fisRes;
	}

	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {

		String name = null;
		TemplateModel paramValue = (TemplateModel) params.get("name");
		if (paramValue != null) {
			name = ((TemplateScalarModel) paramValue).getAsString();
		}
		if (name == null || "".equals(name)) {
			return;
		}
		try {
			String url = fisRes.require(name);
		} catch (FisException e) {
			throw new TemplateException(e.getMessage(), e, env);
		}

	}

}
