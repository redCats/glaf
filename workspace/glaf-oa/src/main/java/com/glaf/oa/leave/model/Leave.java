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
package com.glaf.oa.leave.model;

import java.io.*;
import java.util.*;

import javax.persistence.*;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.glaf.core.base.*;
import com.glaf.oa.leave.util.*;

@Entity
@Table(name = "oa_leave")
public class Leave implements Serializable, JSONable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "leaveid", nullable = false)
	protected Long leaveid;

	@Column(name = "area")
	protected String area;

	@Column(name = "company", length = 100)
	protected String company;

	@Column(name = "dept", length = 100)
	protected String dept;

	@Column(name = "appuser", length = 20)
	protected String appuser;

	@Column(name = "post", length = 100)
	protected String post;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "appdate")
	protected Date appdate;

	@Column(name = "type")
	protected Integer type;

	@Column(name = "content")
	protected String content;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "startdate")
	protected Date startdate;

	@Column(name = "starttime")
	protected Integer starttime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "enddate")
	protected Date enddate;

	@Column(name = "endtime")
	protected Integer endtime;

	@Column(name = "leavesum")
	protected Double leavesum;

	@Column(name = "remark")
	protected String remark;

	@Column(name = "status")
	protected Integer status;

	@Column(name = "processname", length = 100)
	protected String processname;

	@Column(name = "processinstanceid")
	protected Long processinstanceid;

	@Column(name = "wfstatus")
	protected Long wfstatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createDate")
	protected Date createDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateDate")
	protected Date updateDate;

	@Column(name = "createBy", length = 0)
	protected String createBy;

	@Column(name = "updateBy", length = 0)
	protected String updateBy;

	public Leave() {

	}

	public Long getLeaveid() {
		return this.leaveid;
	}

	public String getArea() {
		return this.area;
	}

	public String getCompany() {
		return this.company;
	}

	public String getDept() {
		return this.dept;
	}

	public String getAppuser() {
		return this.appuser;
	}

	public String getPost() {
		return this.post;
	}

	public Date getAppdate() {
		return this.appdate;
	}

	public Integer getType() {
		return this.type;
	}

	public String getContent() {
		return this.content;
	}

	public Date getStartdate() {
		return this.startdate;
	}

	public Integer getStarttime() {
		return this.starttime;
	}

	public Date getEnddate() {
		return this.enddate;
	}

	public Integer getEndtime() {
		return this.endtime;
	}

	public Double getLeavesum() {
		return this.leavesum;
	}

	public String getRemark() {
		return this.remark;
	}

	public Integer getStatus() {
		return this.status;
	}

	public String getProcessname() {
		return this.processname;
	}

	public Long getProcessinstanceid() {
		return this.processinstanceid;
	}

	public Long getWfstatus() {
		return this.wfstatus;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setLeaveid(Long leaveid) {
		this.leaveid = leaveid;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public void setAppuser(String appuser) {
		this.appuser = appuser;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public void setAppdate(Date appdate) {
		this.appdate = appdate;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public void setStarttime(Integer starttime) {
		this.starttime = starttime;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public void setEndtime(Integer endtime) {
		this.endtime = endtime;
	}

	public void setLeavesum(Double leavesum) {
		this.leavesum = leavesum;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setProcessname(String processname) {
		this.processname = processname;
	}

	public void setProcessinstanceid(Long processinstanceid) {
		this.processinstanceid = processinstanceid;
	}

	public void setWfstatus(Long wfstatus) {
		this.wfstatus = wfstatus;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Leave other = (Leave) obj;
		if (leaveid == null) {
			if (other.leaveid != null)
				return false;
		} else if (!leaveid.equals(other.leaveid))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leaveid == null) ? 0 : leaveid.hashCode());
		return result;
	}

	public Leave jsonToObject(JSONObject jsonObject) {
		return LeaveJsonFactory.jsonToObject(jsonObject);
	}

	public JSONObject toJsonObject() {
		return LeaveJsonFactory.toJsonObject(this);
	}

	public ObjectNode toObjectNode() {
		return LeaveJsonFactory.toObjectNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}