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

package com.glaf.core.service.impl;

import java.util.*;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.glaf.core.id.*;
import com.glaf.core.domain.*;
import com.glaf.core.mapper.*;
import com.glaf.core.query.*;
import com.glaf.core.service.*;

@Service("sysLogService")
@Transactional(readOnly = true)
public class MxSysLogServiceImpl implements ISysLogService {
	protected final static Log logger = LogFactory
			.getLog(MxSysLogServiceImpl.class);

	protected IdGenerator idGenerator;

	protected SqlSessionTemplate sqlSessionTemplate;

	protected SysLogMapper sysLogMapper;

	public MxSysLogServiceImpl() {

	}

	public int count(SysLogQuery query) {
		query.ensureInitialized();
		return sysLogMapper.getSysLogCount(query);
	}

	@Transactional
	public boolean create(SysLog bean) {
		this.save(bean);
		return true;
	}

	@Transactional
	public boolean delete(long id) {
		this.deleteById(id);
		return true;
	}

	@Transactional
	public boolean delete(SysLog bean) {
		this.deleteById(bean.getId());
		return true;
	}

	@Transactional
	public void deleteById(Long id) {
		if (id != null) {
			sysLogMapper.deleteSysLogById(id);
		}
	}

	@Transactional
	public void deleteByIds(List<Long> rowIds) {
		if (rowIds != null && !rowIds.isEmpty()) {
			SysLogQuery query = new SysLogQuery();
			query.rowIds(rowIds);
			sysLogMapper.deleteSysLogs(query);
		}
	}

	public SysLog findById(long id) {
		return this.getSysLog(id);
	}

	public SysLog getSysLog(Long id) {
		if (id == null) {
			return null;
		}
		SysLog sysLog = sysLogMapper.getSysLogById(id);
		return sysLog;
	}

	public int getSysLogCountByQueryCriteria(SysLogQuery query) {
		return sysLogMapper.getSysLogCount(query);
	}

	public List<SysLog> getSysLogsByQueryCriteria(int start, int pageSize,
			SysLogQuery query) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		List<SysLog> rows = sqlSessionTemplate.selectList("getSysLogs", query,
				rowBounds);
		return rows;
	}

	public List<SysLog> list(SysLogQuery query) {
		query.ensureInitialized();
		List<SysLog> list = sysLogMapper.getSysLogs(query);
		return list;
	}

	@Transactional
	public void save(SysLog sysLog) {
		if (sysLog.getId() == 0L) {
			sysLog.setId(idGenerator.nextId());
			// sysLog.setCreateDate(new Date());
			sysLogMapper.insertSysLog(sysLog);
		} else {
			sysLogMapper.updateSysLog(sysLog);
		}
	}

	@Resource
	@Qualifier("myBatisDbIdGenerator")
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	@Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Resource
	public void setSysLogMapper(SysLogMapper sysLogMapper) {
		this.sysLogMapper = sysLogMapper;
	}

	@Transactional
	public boolean update(SysLog bean) {
		this.save(bean);
		return true;
	}

}