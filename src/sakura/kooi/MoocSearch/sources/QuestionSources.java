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
import lombok.Setter;
import sakura.kooi.MoocSearch.sources.CxMooc.CXMooc_Tool;
import sakura.kooi.MoocSearch.sources.XuanXiu365.XuanXiu365;
import sakura.kooi.MoocSearch.sources.kbm.KeBangMang;
import sakura.kooi.MoocSearch.utils.AnswerCallback;

@SuppressWarnings("SpellCheckingInspection")
public enum QuestionSources {
	XUANXIU365("xuanxiu365", "题库一") {
		@Override
		public Runnable get(String question, AnswerCallback callback) {
			return new XuanXiu365(question, callback);
		}
	},
	CXMOOCTOOL("cxmooc", "题库二") {
		@Override
		public Runnable get(String question, AnswerCallback callback) {
			return new CXMooc_Tool(question, callback);
		}
	},
	KBM("150s", "题库三") {
		@Override
		public Runnable get(String question, AnswerCallback callback) {
			return new KeBangMang(question, callback);
		}
	};

	@Getter private String name;
	@Getter private String key;
	@Getter @Setter
	private boolean enabled;

	QuestionSources(String key, String name) {
		this.key = key;
		this.name = name;
	}

	public abstract Runnable get(String question, AnswerCallback callback);
}
