/**
 * @Project MoocSearch
 *
 * Copyright 2019 SakuraKooi. All right reserved.
 *
 * This is a private project. Distribution is not allowed.
 * You needs ask SakuraKooi for the permission to using it.
 *
 * @Author SakuraKooi (sakurakoi993519867@gmail.com)
 */
package sakura.kooi.MoocSearch.sources;

import lombok.Getter;
import sakura.kooi.MoocSearch.sources.CxMooc.CXMooc_Tool;
import sakura.kooi.MoocSearch.sources.XuanXiu365.XuanXiu365;
import sakura.kooi.MoocSearch.utils.AnswerCallback;

public enum QuestionSources {
	XUANXIU365("题库一") {
		@Override
		public Runnable get(String question, AnswerCallback callback) {
			return new XuanXiu365(question, callback);
		}
	},
	CXMOOCTOOL("题库二") {
		@Override
		public Runnable get(String question, AnswerCallback callback) {
			return new CXMooc_Tool(question, callback);
		}
	};

	@Getter private String name;

	QuestionSources(final String name) {
		this.name = name;
	}

	public abstract Runnable get(String question, AnswerCallback callback);
}
