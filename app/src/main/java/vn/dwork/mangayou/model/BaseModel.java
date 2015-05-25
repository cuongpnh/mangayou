package vn.dwork.mangayou.model;


public abstract class BaseModel{	
	protected int _id = 0;

	public int getId() {
		return _id;
	}

	public BaseModel setId(int _id) {
		this._id = _id;
		return this;
	}

	public BaseModel() {

	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof BaseModel) {
			return this.getId() == ((BaseModel) o).getId();
		}
		return super.equals(o);
	}
}
