package us.ceka.domain;

public enum League {
	ENG_PREMIER_LEAGUE ("cfEPL", "英超", "1", TYPE.LEAGUE),
	ENG_FA_CUP ("cfEFA", "英足盃", "5", TYPE.CUP),
	ENG_LEAGUE_CUP ("cfELC", "英聯盃", "8", TYPE.CUP),
	ITA_SERIE_A ("cfISA", "意甲", "2", TYPE.LEAGUE),
	ITA_COPPA_ITALIA ("cfIFC", "意盃 ", "9", TYPE.CUP),
	GER_BUNDESLIGA("cfGSL", "德甲", "3", TYPE.LEAGUE),
	GER_DFB_CUP("cfGSC", "德盃", "11", TYPE.CUP),
	ESP_LALIGA("cfSFL", "西甲", "4", TYPE.LEAGUE),
	ESP_COPA_DEL_REY("cfSFC", "西盃 ", "10", TYPE.CUP),
	EUR_CHAMPIONSHIP("cfUCL", "歐聯", "6", TYPE.CUP),
	EUR_EUROPA("cfUEC", "歐霸", "7", TYPE.CUP),
	
	JPN_JLEAGUE("cfJD1", "日聯", "1103", TYPE.LEAGUE),
	JPN_JLEAGUE_CUP("cfJLC", "日聯盃", "1107", TYPE.CUP),
	JPN_EMPEROR_CUP("cfJEC", "日皇盃", "1168", TYPE.CUP),
	ASIA_AFC_CHAMPIONSHIP("cfACL", "亞冠盃", "1109", TYPE.CUP),
	
	ARG_DIVISION_1 ("cfAPL", "阿甲", "1126", TYPE.LEAGUE),
	ARG_COPA_ARGENTINA ("cfAGC", "阿盃 ", "1369", TYPE.CUP),
	BRA_SERIE_A("cfBD1", "巴甲", "1113", TYPE.LEAGUE),
	BRA_COPA_DO_BRASIL("cfBDC", "巴西盃", "1115", TYPE.CUP),
	SOU_COPA_LIBERTADORES("cfCL", "自由盃", "1116", TYPE.CUP),
	SOU_COPA_SUDAMERICANA("cfSAC", "南球盃", "1118", TYPE.CUP),

	FRIENDLY("cfCLB", "球會賽", "0", TYPE.OTHERS),
	OTHERS("others", "其他", "0", TYPE.OTHERS)
	;
	public enum TYPE {LEAGUE, CUP, OTHERS};
	private String code;
	private String name;
	private String id;
	private TYPE type;
	
	public boolean isDomesticLeague() {
		return this.type.equals(TYPE.LEAGUE);
	}
	
	League(String code, String name, String number, TYPE type) {
		this.code = code;
		this.name = name;
		this.id = number;
		this.type = type;
	}
	
	public static League getByCode(String code) {
		for(League l : League.values()) 
			if(l.getCode().equals(code)) return l;
		return null;
	}
	public static League getById(String id) {
		for(League l : League.values())
			if(l.getId().equals(id)) return l;
		return null;
	}
	
	public static League getByName(String name) {
		for(League l : League.values()) 
			if(l.getName().equals(name)) return l;
		return null;		
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.name();
	}
}
