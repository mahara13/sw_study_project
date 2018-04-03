package vmstudy.sw.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "personage_call")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class PersonageCall implements Serializable{

	private static final long serialVersionUID = -607663342125767049L;

	public static String FINISHED_STATUS = "f";
	public static String PROCESSING_STATUS = "p";

	public PersonageCall() {
	}
	
	public PersonageCall(String url){
		this.setUrl(url);
	}
	
	public PersonageCall(String status, Date date){
		this.setStatus(status);
		this.setCreatedAt(date);
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String status = PROCESSING_STATUS ;
	
	private int calls_count = 0;
	
	private int failing_calls_count = 0;
	
	private String url;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCalls_count() {
		return calls_count;
	}

	public void setCalls_count(int calls_count) {
		this.calls_count = calls_count;
	}

	public int getFailing_calls_count() {
		return failing_calls_count;
	}

	public void setFailing_calls_count(int failing_calls_count) {
		this.failing_calls_count = failing_calls_count;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public void incrementCallsCount() {
    	calls_count++;
    }
    
    public void incrementFailingCount() {
    	failing_calls_count++;
    }
    
    public boolean isFinished() {
    	return FINISHED_STATUS.equals(status);
    }
    
    public void setFinishedStatus() {
    	setStatus(FINISHED_STATUS);
    }
}