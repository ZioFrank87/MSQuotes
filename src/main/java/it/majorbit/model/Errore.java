package it.majorbit.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Errore {
	
	@Id
	String code;
	@Column
	Integer httpError;
	@Column
	String textIta;
	@Column
	String textEng;
	
	public Errore() {
		super();
	}

	public Errore(String code, Integer httpError, String textIta, String textEng) {
		super();
		this.code = code;
		this.httpError = httpError;
		this.textIta = textIta;
		this.textEng = textEng;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getHttpError() {
		return httpError;
	}

	public void setHttpError(Integer httpError) {
		this.httpError = httpError;
	}

	public String getTextIta() {
		return textIta;
	}

	public void setTextIta(String textIta) {
		this.textIta = textIta;
	}

	public String getTextEng() {
		return textEng;
	}

	public void setTextEng(String textEng) {
		this.textEng = textEng;
	}

	@Override
	public String toString() {
		return "Errore [code=" + code + ", httpError=" + httpError + ", textIta=" + textIta + ", textEng=" + textEng
				+ "]";
	}
	
	
}
