package tools.locations;

/**
 * An enum containing the states and territorial
 *  regions of the United States of America
 * @author jonah.sloan
 * @author jenna3715
 *
 */
public enum USARegionalCodes
{
	ak("Alaska"),
	al("Alabama"),
	ar("Arkansas"),
	az("Arizona"),
	ca("California"),
	co("Colorado"),
	ct("Connecticut"),
	dc("District of Columbia"),
	de("Delaware"),
	fl("Florida"),
	ga("Georgia"),
	gu("Guam"),
	hi("Hawaii"),
	ia("Iowa"),
	id("Idaho"),
	il("Illinois"),
	in("Indiana"),
	ks("Kansas"),
	ky("Kentucky"),
	la("Louisiana"),
	ma("Massachusetts"),
	md("Maryland"),
	me("Maine"),
	mi("Michigan"),
	mn("Minnesota"),
	mo("Missouri"),
	ms("Mississippi"),
	mt("Montana"),
	nc("North Carolina"),
	nd("North Dakota"),
	ne("Nebraska"),
	nh("New Hampshire"),
	nj("New Jersey"),
	nm("New Mexico"),
	nv("Nevada"),
	ny("New York"),
	oh("Ohio"),
	ok("Oklahoma"),
	or("Oregon"),
	pa("Pennsylvania"),
	ph("Philippine Islands"),//Deprecated
	pr("Puerto Rico"),
	pz("Panama Canal Zone"),//Deprecated
	ri("Rhode Island"),
	sc("South Carolina"),
	sd("South Dakota"),
	tn("Tennessee"),
	tx("Texas"),
	ut("Utah"),
	va("Virginia"),
	vi("U.S. Virgin Islands"),
	vt("Vermont"),
	wa("Washington"),
	wi("Wisconsin"),
	wv("West Virginia"),
	wy("Wyoming"),
	;
	private final String name;
	private USARegionalCodes(String name) {
		this.name=name;
	}
	public String getName() {
		return name;
	}
}
