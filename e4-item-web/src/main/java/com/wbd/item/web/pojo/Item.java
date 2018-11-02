package com.wbd.item.web.pojo;

import com.wbd.pojo.TbItem;

/**
 * 继承TbItem，扩展一个getImages 方法， 因为前台页面需要images属性， 所以必须提供一个 但是TbItem
 * 没有，为了保持原则性，不修接口的属性， 我们可以采用继承的方式。
 * <p>
 * Title: Item.java
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author 朱光和
 * @date 2018年11月2日
 */
public class Item extends TbItem {

	/** serialVersionUID*/ 
	private static final long serialVersionUID = 1L;

	/**
	 * 该方法，提供给页面的images属性使用。
	 * <p>
	 * Title: getImages
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @return
	 */
	public String[] getImages() {
		String image2 = this.getImage();
		if (image2 != null && !"".equals(image2)) {
			String[] strings = image2.split(",");
			return strings;
		}
		return null;
	}

	public Item() {

	}

	public Item(TbItem tbItem) {
		this.setBarcode(tbItem.getBarcode());
		this.setCid(tbItem.getCid());
		this.setCreated(tbItem.getCreated());
		this.setId(tbItem.getId());
		this.setImage(tbItem.getImage());
		this.setNum(tbItem.getNum());
		this.setPrice(tbItem.getPrice());
		this.setSellPoint(tbItem.getSellPoint());
		this.setStatus(tbItem.getStatus());
		this.setTitle(tbItem.getTitle());
		this.setUpdated(tbItem.getUpdated());
	}

}
