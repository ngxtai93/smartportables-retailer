package tai.entity;

public class Deal {
	private String tweet;
	private String productName;
	private DealType dealType;
	private Double amount;
	private String link;
	
	public String getTweet() {
		return tweet;
	}
	public void setTweet(String tweet) {
		this.tweet = tweet;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public DealType getDealType() {
		return dealType;
	}
	public void setDealType(DealType dealType) {
		this.dealType = dealType;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}
