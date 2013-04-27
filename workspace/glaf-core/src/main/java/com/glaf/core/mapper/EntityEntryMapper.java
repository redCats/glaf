/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glaf.core.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.glaf.core.domain.EntityEntry;
import com.glaf.core.domain.EntryPoint;
import com.glaf.core.query.EntityEntryQuery;
import com.glaf.core.query.EntryPointQuery;

@Component
public interface EntityEntryMapper {

	void deleteEntityEntrys(EntityEntryQuery query);

	void deleteEntityEntryById(String id);

	void deleteEntryPoint(String entityEntryId);

	EntityEntry getEntityEntryById(String id);

	int getEntityEntryCount(EntityEntryQuery query);

	List<EntityEntry> getEntityEntries(EntityEntryQuery query);

	List<EntryPoint> getEntryPoints(EntryPointQuery query);

	void insertEntityEntry(EntityEntry model);

	void insertEntryPoint(EntryPoint model);

	void updateEntityEntry(EntityEntry model);

}