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

package com.glaf.base.modules.sys.query;

import java.util.*;
import com.glaf.core.query.DataQuery;

public class SysDepartmentQuery extends DataQuery {
	private static final long serialVersionUID = 1L;
	protected List<Long> rowIds;
	protected String name;
	protected String nameLike;
	protected List<String> names;
	protected String desc;
	protected String descLike;
	protected List<String> descs;
	protected Date createTimeGreaterThanOrEqual;
	protected Date createTimeLessThanOrEqual;
	protected List<Date> createTimes;
	protected Integer sort;
	protected Integer sortGreaterThanOrEqual;
	protected Integer sortGreaterThan;
	protected Integer sortLessThanOrEqual;
	protected Integer sortLessThan;
	protected List<Integer> sorts;
	protected String no;
	protected String noLike;
	protected List<String> nos;
	protected String code;
	protected String codeLike;
	protected List<String> codes;
	protected String code2;

	protected String fincode;
	protected String fincodeLike;
	protected List<String> fincodes;
	protected Long nodeId;
	protected Long nodeIdGreaterThanOrEqual;
	protected Long nodeIdLessThanOrEqual;
	protected List<Long> nodeIds;

	public SysDepartmentQuery() {

	}

	public Integer getSortGreaterThan() {
		return sortGreaterThan;
	}

	public void setSortGreaterThan(Integer sortGreaterThan) {
		this.sortGreaterThan = sortGreaterThan;
	}

	public Integer getSortLessThan() {
		return sortLessThan;
	}

	public void setSortLessThan(Integer sortLessThan) {
		this.sortLessThan = sortLessThan;
	}

	public String getName() {
		return name;
	}

	public String getNameLike() {
		if (nameLike != null && nameLike.trim().length() > 0) {
			if (!nameLike.startsWith("%")) {
				nameLike = "%" + nameLike;
			}
			if (!nameLike.endsWith("%")) {
				nameLike = nameLike + "%";
			}
		}
		return nameLike;
	}

	public List<String> getNames() {
		return names;
	}

	public String getDesc() {
		return desc;
	}

	public String getDescLike() {
		if (descLike != null && descLike.trim().length() > 0) {
			if (!descLike.startsWith("%")) {
				descLike = "%" + descLike;
			}
			if (!descLike.endsWith("%")) {
				descLike = descLike + "%";
			}
		}
		return descLike;
	}

	public List<String> getDescs() {
		return descs;
	}

	public Date getCreateTimeGreaterThanOrEqual() {
		return createTimeGreaterThanOrEqual;
	}

	public Date getCreateTimeLessThanOrEqual() {
		return createTimeLessThanOrEqual;
	}

	public List<Date> getCreateTimes() {
		return createTimes;
	}

	public Integer getSort() {
		return sort;
	}

	public Integer getSortGreaterThanOrEqual() {
		return sortGreaterThanOrEqual;
	}

	public Integer getSortLessThanOrEqual() {
		return sortLessThanOrEqual;
	}

	public List<Integer> getSorts() {
		return sorts;
	}

	public String getNo() {
		return no;
	}

	public String getNoLike() {
		if (noLike != null && noLike.trim().length() > 0) {
			if (!noLike.startsWith("%")) {
				noLike = "%" + noLike;
			}
			if (!noLike.endsWith("%")) {
				noLike = noLike + "%";
			}
		}
		return noLike;
	}

	public List<String> getNos() {
		return nos;
	}

	public String getCode() {
		return code;
	}

	public String getCodeLike() {
		if (codeLike != null && codeLike.trim().length() > 0) {
			if (!codeLike.startsWith("%")) {
				codeLike = "%" + codeLike;
			}
			if (!codeLike.endsWith("%")) {
				codeLike = codeLike + "%";
			}
		}
		return codeLike;
	}

	public List<String> getCodes() {
		return codes;
	}

	public String getCode2() {
		return code2;
	}

	public String getFincode() {
		return fincode;
	}

	public String getFincodeLike() {
		if (fincodeLike != null && fincodeLike.trim().length() > 0) {
			if (!fincodeLike.startsWith("%")) {
				fincodeLike = "%" + fincodeLike;
			}
			if (!fincodeLike.endsWith("%")) {
				fincodeLike = fincodeLike + "%";
			}
		}
		return fincodeLike;
	}

	public List<String> getFincodes() {
		return fincodes;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public Long getNodeIdGreaterThanOrEqual() {
		return nodeIdGreaterThanOrEqual;
	}

	public Long getNodeIdLessThanOrEqual() {
		return nodeIdLessThanOrEqual;
	}

	public List<Long> getNodeIds() {
		return nodeIds;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setDescLike(String descLike) {
		this.descLike = descLike;
	}

	public void setDescs(List<String> descs) {
		this.descs = descs;
	}

	public void setCreateTimeGreaterThanOrEqual(
			Date createTimeGreaterThanOrEqual) {
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
	}

	public void setCreateTimeLessThanOrEqual(Date createTimeLessThanOrEqual) {
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
	}

	public void setCreateTimes(List<Date> createTimes) {
		this.createTimes = createTimes;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public void setSortGreaterThanOrEqual(Integer sortGreaterThanOrEqual) {
		this.sortGreaterThanOrEqual = sortGreaterThanOrEqual;
	}

	public void setSortLessThanOrEqual(Integer sortLessThanOrEqual) {
		this.sortLessThanOrEqual = sortLessThanOrEqual;
	}

	public void setSorts(List<Integer> sorts) {
		this.sorts = sorts;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setNoLike(String noLike) {
		this.noLike = noLike;
	}

	public void setNos(List<String> nos) {
		this.nos = nos;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCodeLike(String codeLike) {
		this.codeLike = codeLike;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}

	public void setCode2(String code2) {
		this.code2 = code2;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setStatusGreaterThanOrEqual(Integer statusGreaterThanOrEqual) {
		this.statusGreaterThanOrEqual = statusGreaterThanOrEqual;
	}

	public void setStatusLessThanOrEqual(Integer statusLessThanOrEqual) {
		this.statusLessThanOrEqual = statusLessThanOrEqual;
	}

	public void setFincode(String fincode) {
		this.fincode = fincode;
	}

	public void setFincodeLike(String fincodeLike) {
		this.fincodeLike = fincodeLike;
	}

	public void setFincodes(List<String> fincodes) {
		this.fincodes = fincodes;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public void setNodeIdGreaterThanOrEqual(Long nodeIdGreaterThanOrEqual) {
		this.nodeIdGreaterThanOrEqual = nodeIdGreaterThanOrEqual;
	}

	public void setNodeIdLessThanOrEqual(Long nodeIdLessThanOrEqual) {
		this.nodeIdLessThanOrEqual = nodeIdLessThanOrEqual;
	}

	public void setNodeIds(List<Long> nodeIds) {
		this.nodeIds = nodeIds;
	}

	public SysDepartmentQuery name(String name) {
		if (name == null) {
			throw new RuntimeException("name is null");
		}
		this.name = name;
		return this;
	}

	public SysDepartmentQuery nameLike(String nameLike) {
		if (nameLike == null) {
			throw new RuntimeException("name is null");
		}
		this.nameLike = nameLike;
		return this;
	}

	public SysDepartmentQuery names(List<String> names) {
		if (names == null) {
			throw new RuntimeException("names is empty ");
		}
		this.names = names;
		return this;
	}

	public SysDepartmentQuery desc(String desc) {
		if (desc == null) {
			throw new RuntimeException("desc is null");
		}
		this.desc = desc;
		return this;
	}

	public SysDepartmentQuery descLike(String descLike) {
		if (descLike == null) {
			throw new RuntimeException("desc is null");
		}
		this.descLike = descLike;
		return this;
	}

	public SysDepartmentQuery descs(List<String> descs) {
		if (descs == null) {
			throw new RuntimeException("descs is empty ");
		}
		this.descs = descs;
		return this;
	}

	public SysDepartmentQuery createTimeGreaterThanOrEqual(
			Date createTimeGreaterThanOrEqual) {
		if (createTimeGreaterThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeGreaterThanOrEqual = createTimeGreaterThanOrEqual;
		return this;
	}

	public SysDepartmentQuery createTimeLessThanOrEqual(
			Date createTimeLessThanOrEqual) {
		if (createTimeLessThanOrEqual == null) {
			throw new RuntimeException("createTime is null");
		}
		this.createTimeLessThanOrEqual = createTimeLessThanOrEqual;
		return this;
	}

	public SysDepartmentQuery createTimes(List<Date> createTimes) {
		if (createTimes == null) {
			throw new RuntimeException("createTimes is empty ");
		}
		this.createTimes = createTimes;
		return this;
	}

	public SysDepartmentQuery sort(Integer sort) {
		if (sort == null) {
			throw new RuntimeException("sort is null");
		}
		this.sort = sort;
		return this;
	}

	public SysDepartmentQuery sortGreaterThanOrEqual(
			Integer sortGreaterThanOrEqual) {
		if (sortGreaterThanOrEqual == null) {
			throw new RuntimeException("sort is null");
		}
		this.sortGreaterThanOrEqual = sortGreaterThanOrEqual;
		return this;
	}

	public SysDepartmentQuery sortLessThanOrEqual(Integer sortLessThanOrEqual) {
		if (sortLessThanOrEqual == null) {
			throw new RuntimeException("sort is null");
		}
		this.sortLessThanOrEqual = sortLessThanOrEqual;
		return this;
	}

	public SysDepartmentQuery sorts(List<Integer> sorts) {
		if (sorts == null) {
			throw new RuntimeException("sorts is empty ");
		}
		this.sorts = sorts;
		return this;
	}

	public SysDepartmentQuery no(String no) {
		if (no == null) {
			throw new RuntimeException("no is null");
		}
		this.no = no;
		return this;
	}

	public SysDepartmentQuery noLike(String noLike) {
		if (noLike == null) {
			throw new RuntimeException("no is null");
		}
		this.noLike = noLike;
		return this;
	}

	public SysDepartmentQuery nos(List<String> nos) {
		if (nos == null) {
			throw new RuntimeException("nos is empty ");
		}
		this.nos = nos;
		return this;
	}

	public SysDepartmentQuery code(String code) {
		if (code == null) {
			throw new RuntimeException("code is null");
		}
		this.code = code;
		return this;
	}

	public SysDepartmentQuery codeLike(String codeLike) {
		if (codeLike == null) {
			throw new RuntimeException("code is null");
		}
		this.codeLike = codeLike;
		return this;
	}

	public SysDepartmentQuery codes(List<String> codes) {
		if (codes == null) {
			throw new RuntimeException("codes is empty ");
		}
		this.codes = codes;
		return this;
	}

	public SysDepartmentQuery code2(String code2) {
		if (code2 == null) {
			throw new RuntimeException("code2 is null");
		}
		this.code2 = code2;
		return this;
	}

	public SysDepartmentQuery fincode(String fincode) {
		if (fincode == null) {
			throw new RuntimeException("fincode is null");
		}
		this.fincode = fincode;
		return this;
	}

	public SysDepartmentQuery fincodeLike(String fincodeLike) {
		if (fincodeLike == null) {
			throw new RuntimeException("fincode is null");
		}
		this.fincodeLike = fincodeLike;
		return this;
	}

	public SysDepartmentQuery fincodes(List<String> fincodes) {
		if (fincodes == null) {
			throw new RuntimeException("fincodes is empty ");
		}
		this.fincodes = fincodes;
		return this;
	}

	public SysDepartmentQuery nodeId(Long nodeId) {
		if (nodeId == null) {
			throw new RuntimeException("nodeId is null");
		}
		this.nodeId = nodeId;
		return this;
	}

	public SysDepartmentQuery nodeIdGreaterThanOrEqual(
			Long nodeIdGreaterThanOrEqual) {
		if (nodeIdGreaterThanOrEqual == null) {
			throw new RuntimeException("nodeId is null");
		}
		this.nodeIdGreaterThanOrEqual = nodeIdGreaterThanOrEqual;
		return this;
	}

	public SysDepartmentQuery nodeIdLessThanOrEqual(Long nodeIdLessThanOrEqual) {
		if (nodeIdLessThanOrEqual == null) {
			throw new RuntimeException("nodeId is null");
		}
		this.nodeIdLessThanOrEqual = nodeIdLessThanOrEqual;
		return this;
	}

	public SysDepartmentQuery nodeIds(List<Long> nodeIds) {
		if (nodeIds == null) {
			throw new RuntimeException("nodeIds is empty ");
		}
		this.nodeIds = nodeIds;
		return this;
	}

	public String getOrderBy() {
		if (sortColumn != null) {
			String a_x = " asc ";
			if (sortOrder != null) {
				a_x = sortOrder;
			}

			if ("name".equals(sortColumn)) {
				orderBy = "E.NAME" + a_x;
			}

			if ("desc".equals(sortColumn)) {
				orderBy = "E.DEPTDESC" + a_x;
			}

			if ("createTime".equals(sortColumn)) {
				orderBy = "E.CREATETIME" + a_x;
			}

			if ("sort".equals(sortColumn)) {
				orderBy = "E.SORT" + a_x;
			}

			if ("no".equals(sortColumn)) {
				orderBy = "E.DEPTNO" + a_x;
			}

			if ("code".equals(sortColumn)) {
				orderBy = "E.CODE" + a_x;
			}

			if ("code2".equals(sortColumn)) {
				orderBy = "E.CODE2" + a_x;
			}

			if ("status".equals(sortColumn)) {
				orderBy = "E.STATUS" + a_x;
			}

			if ("fincode".equals(sortColumn)) {
				orderBy = "E.FINCODE" + a_x;
			}

			if ("nodeId".equals(sortColumn)) {
				orderBy = "E.NODEID" + a_x;
			}

		}
		return orderBy;
	}

	@Override
	public void initQueryColumns() {
		super.initQueryColumns();
		addColumn("id", "ID");
		addColumn("name", "NAME");
		addColumn("desc", "DEPTDESC");
		addColumn("createTime", "CREATETIME");
		addColumn("sort", "SORT");
		addColumn("no", "DEPTNO");
		addColumn("code", "CODE");
		addColumn("code2", "CODE2");
		addColumn("status", "STATUS");
		addColumn("fincode", "FINCODE");
		addColumn("nodeId", "NODEID");
	}

}