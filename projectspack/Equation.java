package projectspack;

import static projectspack.Util.isNum;
import static projectspack.Util.isBasicOperator;
import static projectspack.Util.pow;
import static projectspack.Util.df;
import static projectspack.Util.hasTwoAnswers;
import static projectspack.Util.specialfunctions;

import java.util.HashMap;
import java.util.Map;


public class Equation{
	HashMap<Double, Boolean> value =new HashMap<Double,Boolean>();
	String a = "";
	double valStore;

	public Equation(String string,double xVal) {
		valStore = xVal;
		a = string;
		a= string.replaceAll(" ", ""); 
		for(int i = 0 ; i < a.length(); i++ ) {
			if( a.charAt(i)=='(' || a.charAt(i)=='x') {
				if(i !=0) {
					if(isNum(a.charAt(i-1)) || a.charAt(i-1) == ')'){
						a =a.substring(0, i) + "*" +a.substring( i) ;
					}


				}
			}
			if( a.charAt(i)==')'||a.charAt(i)=='x') {
				if(i !=a.length()-1) {
					if(isNum(a.charAt(i+1)) || a.charAt(i+1) == '(' ){
						a =a.substring(0, i+1) + "*" +a.substring( i+1) ;
					}


				}
			}
		}



		if(xVal!= Double.NaN)
			a= a.replaceAll("x", "("+xVal+ ")");
		try {
			sove(xVal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
	public Equation(String string,double xVal,boolean auto) {
		a = string;
		a= string.replaceAll(" ", ""); 
		for(int i = 0 ; i < a.length(); i++ ) {
			if( a.charAt(i)=='(' || a.charAt(i)=='x') {
				if(i !=0) {
					if(isNum(a.charAt(i-1)) || a.charAt(i-1) == ')'){
						a =a.substring(0, i) + "*" +a.substring( i) ;
					}


				}
			}
			if( a.charAt(i)==')'||a.charAt(i)=='x') {
				if(i !=a.length()-1) {
					if(isNum(a.charAt(i+1)) || a.charAt(i+1) == '(' ){
						a =a.substring(0, i+1) + "*" +a.substring( i+1) ;
					}


				}
			}
		}



		if(xVal!= Double.NaN)
			a= a.replaceAll("x", "("+xVal+ ")");
	}
	public void addVar( String var, String val) {
		for(int i = 0 ; i <a.length() - var.length()+1; i++) {
			if(a.substring(i , i + var.length()).equals(var)) {
				System.out.println(a.charAt(i -1));
				if(i==0||isBasicOperator( a.charAt(i -1))  ||a.charAt(i -1)==')') {
					if(i==a.length() - var.length()||isBasicOperator( a.charAt(i +var.length()))  ||a.charAt(i +var.length() )=='(') {
						
						a=a.substring(0 , i ) + (i!=0 && a.charAt(i -1)==')' ? "*":"" ) + val +(i!=a.length() - var.length() &&a.charAt(i +var.length())=='('? "*":"" )+ a.substring( i + var.length());
					}
				}else
					a=a.substring(0 , i ) +"*"+ val + a.substring( i + var.length());
			}
		}


	}
	public void sub(double n) {
		if(value.size()==0) {
			value.put(-n,true);
			return;
		}
		HashMap<Double, Boolean> newVal =new HashMap<Double,Boolean>();
		for(Map.Entry<Double, Boolean> entry : value.entrySet()) {
			newVal.put(entry.getKey() - n, true);
		}
		value = newVal;

	}
	public void add(double n) {
		if(value.size()==0) {
			value.put(n,true);
			return;
		}
		HashMap<Double, Boolean> newVal =new HashMap<Double,Boolean>();
		for(Map.Entry<Double, Boolean> entry : value.entrySet()) {
			newVal.put(entry.getKey() + n, true);
		}
		value = newVal;

	}
	public HashMap<Double, Boolean> function(String function, String[] args) {
		Equation temp;
		HashMap<Double, Boolean> newVal = new HashMap<Double, Boolean>();
		switch(function) {


		case "acos":
			temp = new Equation(args[0],Double.NaN);


			for(Map.Entry<Double, Boolean> entry : temp.value.entrySet()) {
				Double key = entry.getKey();
				newVal.put(Math.acos(key), null);


			}
			return newVal;
		case "asin":
			temp = new Equation(args[0],Double.NaN);


			for(Map.Entry<Double, Boolean> entry : temp.value.entrySet()) {
				Double key = entry.getKey();
				newVal.put(Math.asin(key), null);


			}
			return newVal;
		case "atan":
			temp = new Equation(args[0],Double.NaN);


			for(Map.Entry<Double, Boolean> entry : temp.value.entrySet()) {
				Double key = entry.getKey();
				newVal.put(Math.atan(key), null);
			}
			return newVal;
		case "plusminus":
			temp = new Equation(args[0],Double.NaN);


			for(Map.Entry<Double, Boolean> entry : temp.value.entrySet()) {
				Double key = entry.getKey();
				newVal.put(key, null);
				newVal.put(-key, null);


			}
			return newVal;
		case "sin":
			temp= new Equation(args[0],Double.NaN);


			for(Map.Entry<Double, Boolean> entry : temp.value.entrySet()) {
				Double key = entry.getKey();
				newVal.put(Math.sin(key), null);


			}
			return newVal;
		case "cos":
			temp= new Equation(args[0],Double.NaN);


			for(Map.Entry<Double, Boolean> entry : temp.value.entrySet()) {
				Double key = entry.getKey();
				newVal.put(Math.cos(key), null);


			}
			return newVal;
		case "tan":
			temp= new Equation(args[0],Double.NaN);


			for(Map.Entry<Double, Boolean> entry : temp.value.entrySet()) {
				Double key = entry.getKey();
				newVal.put(Math.tan(key), null);


			}
			return newVal;


		case "floor":
			temp = new Equation(args[0],Double.NaN);


			for(Map.Entry<Double, Boolean> entry : temp.value.entrySet()) {
				Double key = entry.getKey();
				newVal.put(Math.floor(key), null);
			}
			return newVal;
		case "sum":
			try {
				int target = Integer.parseInt(args[1] );
				double sum =0;
				for(int n= Integer.parseInt(args[0] ); n <= target; n++) {

					temp = new Equation(args[2],Double.NaN,false);
					temp.addVar("n", ""+n);
					temp.sove(valStore);

					for(Map.Entry<Double, Boolean> entry : temp.value.entrySet()) {
						sum+= entry.getKey();
						break;
					}
				}
				newVal.put(sum, null);
			}catch(Exception e) {
			}
			return newVal;
		case "abs":
			temp = new Equation(args[0],Double.NaN);


			for(Map.Entry<Double, Boolean> entry : temp.value.entrySet()) {
				Double key = entry.getKey();
				newVal.put(Math.abs(key), null);
			}
			return newVal;
		}
		return newVal;
	}
	public void clipper() {

	}

	public void sove(double xVal) throws Exception{
		/**( 2 )
        |
        V
 	1  + 1
  /          \
x+1          x+1
		 */

		int brack = 0;
		for(int i = 0 ; i < a.length();i++) {
			if(a.charAt(i) == '(') {
				brack++;
			}if(a.charAt(i) == ')') {
				brack--;
			}
		}
		if(brack!= 0 ) {
			return;
		}




		//TODO : Special Functions here


		for(int i = 0 ; i < specialfunctions.length ; i++) {
			String cur = specialfunctions[i];
			while(a.contains(cur)) {
				int index = a.indexOf(cur)+ cur.length();
				if(a.charAt(index) != '(') {

					// invalid formatting
					return ;

				}

				brack = 0;
				int i1 = 0;
				for( i1=index; i1 < a.length();i1++) {
					if(a.charAt(i1) == '(') {
						brack++;
					}if(a.charAt(i1) == ')') {
						brack--;
					}
					if(brack==0) {
						break;
					}
				}
				String[] args = a.substring(index+1,i1).split(",");
				HashMap<Double, Boolean> output = function(cur, args);
				for(Map.Entry<Double, Boolean> entry : output.entrySet()) {
					Double key = entry.getKey();

					String newstring =a.substring(0, index- cur.length() )  
							+df.format( key)
							+a.substring(i1+1);

					Equation temp= new Equation(newstring , xVal);

					for(Map.Entry<Double, Boolean> childValues : temp.value.entrySet()) {
						if(!Double.isNaN(childValues.getKey()))
							value.put(childValues.getKey(), true);
					}
					// do what you have to do here
					// In your case, another loop.
				}
				return;


			}
		}



		while(a.contains("(") &&a.contains(")")) {

			int num =0;
			int start=0;

			for(int i = a.indexOf("(") ; i < a.length() ; i++ ) {
				if( a.charAt(i) == '(') {
					if(num == 0)
					{
						start = i;
					}
					num ++;

				}else if( a.charAt(i) == ')') {
					if(num == 1)
					{

						Equation b = new Equation(a.substring(start+1,i ) ,Double.NaN);
						if(b.value.size()==0) {

							return;
						}
						if(i+2 < a.length()) {
							if(a.charAt(i+1) == '^' ) {
								if(a.charAt(i+2)!='(') {

									int s ;

									for(s =i +2 ; s < a.length() ; s++) {
										if(a.charAt(s-1) == 'E') 
											continue;
										if(a.charAt(s) == '-'&& s==i+2 )continue;
										if(isBasicOperator(a.charAt(s)))break;
									}
									double expo= Double.parseDouble(a.substring(i+2, s));
									if(!hasTwoAnswers(expo)) {


										for(Map.Entry<Double, Boolean> entry : b.value.entrySet()) {
											Double key = entry.getKey();

											String newstring =a.substring(0, start )  
													+df.format( pow(key, expo) )
													+a.substring(s);
											Equation temp= new Equation(newstring , xVal);

											for(Map.Entry<Double, Boolean> childValues : temp.value.entrySet()) {
												if(!Double.isNaN(childValues.getKey()))
													value.put(childValues.getKey(), true);
											}
											// do what you have to do here
											// In your case, another loop.
										}
										return ;

									}else {

										for(Map.Entry<Double, Boolean> entry : b.value.entrySet()) {
											Double key = entry.getKey();

											String newstring =a.substring(0, start )  
													+df.format( pow(key, expo) )
													+a.substring(s);
											Equation temp= new Equation(newstring , xVal);
											for(Map.Entry<Double, Boolean> childValues : temp.value.entrySet()) {
												if(!Double.isNaN(childValues.getKey()))
													value.put(childValues.getKey(), true);
											}
											// do what you have to do here
											// In your case, another loop.
										}
										for(Map.Entry<Double, Boolean> entry : b.value.entrySet()) {
											Double key = entry.getKey();

											String newstring =a.substring(0, start )  + "(-"
													+df.format( pow(key, expo) )+")"
													+a.substring(s);
											Equation temp= new Equation(newstring , xVal);
											for(Map.Entry<Double, Boolean> childValues : temp.value.entrySet()) {
												value.put(childValues.getKey(), true);
											}
											// do what you have to do here
											// In your case, another loop.
										}
										return ;
									}
								}else {

								}
							}
						}
						for(Map.Entry<Double, Boolean> entry : b.value.entrySet()) {

							Equation temp = new Equation(a.substring(0, start )  
									+df.format(entry.getKey() )
									+a.substring(i+1), xVal);

							for(Map.Entry<Double, Boolean> childValues : temp.value.entrySet()) {
								if(!Double.isNaN(childValues.getKey()))
									value.put(childValues.getKey(), true);
							}
							return;
						}
						break;
					}
					num --;

				}
			}

		}
		while(a.contains("^")) {
			int inde = a.indexOf("^");

			int e ;
			for(e=inde-1 ; e >=0 ; e --) {

				if(isBasicOperator(a.charAt(e)))break;

			}
			int s ;

			for(s =inde +1 ; s < a.length() ; s++) {
				if(a.charAt(s-1) == 'E') 
					continue;
				if(a.charAt(s) == '-'&& s==inde+1 )continue;
				if(isBasicOperator(a.charAt(s)))break;
			}
			double expo =Double.parseDouble(a.substring(inde+1, s));
			if(hasTwoAnswers(expo)) {// If it has two answers then it would create an two equations exactly the same as itself but with +- and solve it and return it
				String newstring =a.substring(0, e+1 )  
						+df.format(pow(Double.parseDouble(a.substring(e+1, inde))
								, expo)) 
						+a.substring(s);

				Equation temp= new Equation(newstring , xVal);
				for(Map.Entry<Double, Boolean> childValues : temp.value.entrySet()) {
					value.put(childValues.getKey(), true);
				}

				newstring =a.substring(0, e+1 )  + "(-"
						+df.format(pow(Double.parseDouble(a.substring(e+1, inde))
								, expo)) +")"
								+a.substring(s);

				temp= new Equation(newstring , xVal);
				for(Map.Entry<Double, Boolean> childValues : temp.value.entrySet()) {
					value.put(childValues.getKey(), true);
				}
				return;
			}
			a = a.substring(0, e+1 )  
					+df.format(pow(Double.parseDouble(a.substring(e+1, inde))
							, expo)) 
					+a.substring(s);

		}

		while(a.contains("*")) {
			int inde = a.indexOf("*");

			int e ;
			for(e=inde-1 ; e >=0 ; e --) {


				if(a.charAt(e) == '-') {

					if(e!=0 &&a.charAt(e-1) == 'E') 
						continue;
					if(e-1 <0) {
						continue;
					}
					if( !isNum(a.charAt(e-1)) )
						continue;
				}
				if(isBasicOperator(a.charAt(e)))break;

			}
			int s ;

			for(s =inde +1 ; s < a.length() ; s++) {
				if(a.charAt(s-1) == 'E') 
					continue;
				if(a.charAt(s) == '-'&& s==inde+1 )continue;
				if(isBasicOperator(a.charAt(s)))break;
			}

			a = a.substring(0, e+1 )  
					+df.format(Double.parseDouble(a.substring(e+1, inde))
							* Double.parseDouble(a.substring(inde+1, s))) 
					+a.substring(s);

		}

		while(a.contains("/")) {
			int inde = a.indexOf("/");

			int e ;
			for(e=inde-1 ; e >=0 ; e --) {

				if(a.charAt(e) == '-') {


					if(e!=0 &&a.charAt(e-1) == 'E') 
						continue;
					if(e-1 <0) {
						continue;
					}
					if( !isNum(a.charAt(e-1)) )
						continue;
				}
				if(isBasicOperator(a.charAt(e)))break;

			}
			int s ;

			for(s =inde +1 ; s < a.length() ; s++) {
				if(a.charAt(s-1) == 'E') 
					continue;
				if(a.charAt(s) == '-'&& s==inde+1 )continue;
				if(isBasicOperator(a.charAt(s)))break;
			}

			a = a.substring(0, e+1 )  
					+df.format(Double.parseDouble(a.substring(e+1, inde))
							/ Double.parseDouble(a.substring(inde+1, s))) 
					+a.substring(s);

		}
		while(a.contains("+")) {
			int inde = a.indexOf("+");

			int e ;
			for(e=inde-1 ; e >=0 ; e --) {

				if(a.charAt(e) == '-') {

					if(e!=0 &&a.charAt(e-1) == 'E') 
						continue;
					if(e-1 <0) {
						continue;
					}
					if( !isNum(a.charAt(e-1)) )
						continue;
				}
				if(isBasicOperator(a.charAt(e)))break;

			}
			int s ;

			for(s =inde +1 ; s < a.length() ; s++) {
				if(a.charAt(s-1) == 'E') 
					continue;
				if(a.charAt(s) == '-'&& s==inde+1 )continue;
				if(isBasicOperator(a.charAt(s)))break;
			}

			a = a.substring(0, e+1 )  
					+df.format(Double.parseDouble(a.substring(e+1, inde))
							+ Double.parseDouble(a.substring(inde+1, s))) 
					+a.substring(s);

		}
		while(a.contains("-")) {
			int inde = a.indexOf("-");

			int e ;
			for(e=inde-1 ; e >=0 ; e --) {

				if(a.charAt(e) == '-') {
					if(e-1 <0) {
						continue;
					}
					if(e!=0 &&a.charAt(e-1) == 'E') 
						continue;

					if( !isNum(a.charAt(e-1)) )
						continue;
				}
				if(isBasicOperator(a.charAt(e)))break;

			}
			int s ;

			for(s =inde +1 ; s < a.length() ; s++) {
				if(a.charAt(s-1) == 'E') 
					continue;
				if(a.charAt(s) == '-'&& s==inde+1 )continue;
				if(isBasicOperator(a.charAt(s)))break;
			}
			if(inde ==0) {
				sub(Double.parseDouble(a.substring(1, s)))  ;
				a=a.substring(s);
				continue;
			}

			a = a.substring(0, e+1 )  
					+df.format(Double.parseDouble(a.substring(e+1, inde))
							- Double.parseDouble(a.substring(inde+1, s))) 
					+a.substring(s);

		}
		if(a.isEmpty()) {
			return ;
		}
		add( Double.parseDouble(a));
		return ;
	}

}