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
package com.github.ipaas.ifw.cache.distributed.memcached.spy;

import org.junit.Before;
import org.junit.BeforeClass;

import com.github.ipaas.ifw.cache.DistributedCacheServiceTest;


/**
 * @author Chenql
 * 
 */
public class FwSpyDirectMemcachedServiceTest extends DistributedCacheServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {
		FwSpyDirectMemcachedService fwSpyDirectMemcachedService = new FwSpyDirectMemcachedService();
		fwSpyDirectMemcachedService.setServerUrl("192.168.75.128:11211");
		dcs = fwSpyDirectMemcachedService;
		super.setUp();
	}

}
