package com.poc.requestapproval.service.util;

import java.util.Collection;

public class Page<T> {

	private Collection<T> currentPage;
	private Integer offset;
	private Integer limit;
	private Long totalCount;

	public Page() {
		//constructor for deserialization
	}

	public Collection<T> getCurrentPage() {
		return currentPage;
	}

	public Integer getOffset() {
		return offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public static class Builder<T> {

		private Collection<T> currentPage;
		private Integer offset;
		private Integer limit;
		private Long totalCount;

		private Builder() {
		}

		public static <T> Builder<T> page() {
			return new Builder<>();
		}

		@SuppressWarnings("squid:S1172")
		public static <T> Builder<T> page(Class<T> type) {
			return new Builder<>();
		}

		public Builder<T> withCurrentPage(Collection<T> currentPage) {
			this.currentPage = currentPage;
			return this;
		}

		public Builder<T> withOffset(Integer offset) {
			this.offset = offset;
			return this;
		}

		public Builder<T> withLimit(Integer limit) {
			this.limit = limit;
			return this;
		}

		public Builder<T> withTotalCount(Long totalCount) {
			this.totalCount = totalCount;
			return this;
		}

		public Page<T> build() {
			Page<T> page = new Page<>();
			page.currentPage = this.currentPage;
			page.offset = this.offset;
			page.limit = this.limit;
			page.totalCount = this.totalCount;
			return page;
		}
	}
}
