package com.mudxx.tool.wx.official.message.resp;

import java.util.List;

/**
 * description: 文本消息
 * @author laiwen
 * @date 2020-01-08 11:39:16
 */
public class NewsMessage extends BaseMessage {

	/**
	 * 图文消息个数，限制为10条以内
	 */
	private Integer ArticleCount;

	/**
	 * 多条图文消息信息，默认第一个item为大图
	 */
	private List<Article> Articles;

	public Integer getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(Integer articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}
	public void setArticles(List<Article> articles) {
		Articles = articles;
	}

}
