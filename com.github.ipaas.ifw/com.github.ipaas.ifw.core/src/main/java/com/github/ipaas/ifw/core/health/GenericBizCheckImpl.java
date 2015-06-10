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

package com.github.ipaas.ifw.core.health;


/**
 *
 * 缺省Heavy检查实现类
 * @author Chenql
 *
 */
public class GenericBizCheckImpl implements BizCheck {

    /* (non-Javadoc)
     * @see cn.tianya.fw.mbean.usability.ice.service.BizCheck#checkBiz()
     */
    @Override
    public String checkBiz() {
        return "1";
    }

}
