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
import sakura.kooi.MoocSearch.sources.hkxy.HuaKaiXiaoYuan1;
import sakura.kooi.MoocSearch.sources.hkxy.HuaKaiXiaoYuan2;
import sakura.kooi.MoocSearch.sources.hkxy.HuaKaiXiaoYuan3;
import sakura.kooi.MoocSearch.sources.iytwl.Iytwl;
import sakura.kooi.MoocSearch.sources.kbm.KeBangMang;
import sakura.kooi.MoocSearch.sources.shuakela.ShuaKeLa;
import sakura.kooi.MoocSearch.sources.wk92e.Moecoo;
import sakura.kooi.MoocSearch.utils.AnswerCallback;

@SuppressWarnings("SpellCheckingInspection")
public enum QuestionSources {
	XUANXIU365_COM("xuanxiu365", "题库一") { // http://tiku.xuanxiu365.com/
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
	WWW_150S_CN("150s", "题库三") { // https://www.150s.cn/
		@Override
		public Runnable get(String question, AnswerCallback callback) {
			return new KeBangMang(question, callback);
		}
	},
	FM210_CN_1("fm210-1", "题库四") { // https://jk.fm210.cn/
		@Override
		public Runnable get(String question, AnswerCallback callback) {
			return new HuaKaiXiaoYuan1(question, callback);
		}
	},
	FM210_CN_2("fm210-2", "题库五") { // https://jk.fm210.cn/
		@Override
		public Runnable get(String question, AnswerCallback callback) {
			return new HuaKaiXiaoYuan2(question, callback);
		}
	},
	FM210_CN_3("fm210-3", "题库六") { // https://jk.fm210.cn/
		@Override
		public Runnable get(String question, AnswerCallback callback) {
			return new HuaKaiXiaoYuan3(question, callback);
		}
	},
	IYTWL_CN("iytwl", "题库十") { // https://api.iytwl.cn/doc/wangke.html
		@Override
		public Runnable get(String question, AnswerCallback callback) {
			return new Iytwl(question, callback);
		}
	},
	WK_92E_WIN("wk92e", "题库十一") { // https://wk.92e.win/wk.html
		@Override
		public Runnable get(String question, AnswerCallback callback) {
			return new Moecoo(question, callback);
		}
	},
	SHUAKELA_TOP("shuakela", "题库十四") { // http://ct.shuakela.top/
		@Override
		public Runnable get(String question, AnswerCallback callback) {
			return new ShuaKeLa(question, callback);
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
