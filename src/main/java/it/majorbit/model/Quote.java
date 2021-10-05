package it.majorbit.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
public class Quote {

	@Id
	@GeneratedValue
	private Integer id;	
	@Column
	private String quote;

	public Quote() {
		super();
	}

	public Quote(String quote) {
		super();
		this.quote = quote;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	@Override
	public String toString() {
		return "Quote [id=" + id + ", quote=" + quote + "]";
	}
	
}
