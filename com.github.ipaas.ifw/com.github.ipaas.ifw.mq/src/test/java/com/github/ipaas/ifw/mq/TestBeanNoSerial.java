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
 */

package com.github.ipaas.ifw.mq;

/**
 *
 * @author Chenql
 */
public class TestBeanNoSerial {

	public long id;

	public String content;

	public TestBeanNoSerial() {
	}

	public TestBeanNoSerial(long id, String content) {
		this.id = id;
		this.content = content;
	}

	@Override
	public String toString() {
		String ret = "Bean: id[" + id + "] content[" + content + "]";
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TestBeanNoSerial) {
			TestBeanNoSerial bean = (TestBeanNoSerial) obj;
			if (id == bean.id && content.equals(bean.content)) {
				return true;
			}
		}
		return false;
	}
}