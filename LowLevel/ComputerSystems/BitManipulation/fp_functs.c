/* Fill in your Name and GNumber in the following two comment fields
 * Name: Matthew Souvannaphandhu
 * GNumber: 01187346
 */
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "fp.h"
fp_gmu add_vals(fp_gmu source1, fp_gmu source2); 
fp_gmu compute_fp(float val);
float get_fp(fp_gmu val);
fp_gmu mult_vals(fp_gmu source1, fp_gmu source2);
fp_gmu add_vals(fp_gmu source1, fp_gmu source2); 

int getBit(fp_gmu b0, int n)
{
	int bit = (b0 >> n) & 1;
	return bit;
}

float getNum(float x)/*changes float into int format*/	 
{
	if (x-1 > 0)
	{
		float y = x - (x-1);
		return y;
	}
	return 0;
}

int getExp(fp_gmu val)                  /*gets exp part value from fp_gmu*/
{
	int i = 0; int x = 32; int exp = 0; 
	for (i = 13; i > 7; i--)
	{
		exp += x * getBit(val, i);
		x = x/2;
	}
	return exp;	
}
float getFracGMU(fp_gmu val)
{
	int i = 0; float frac = 0; 
	float f = .5;
	for (i = 8; i >= 0; i--)
	{
		frac += f* getBit(val, i);
		f = f/2;
	}
	return frac;
}


float getFrac(float x) /* gets the fraction from a fp*/
{
	int y = x;
	return x-y;
}

int normalE(float n) /*returns e part of normalized m*/
{
	int e = 0;
	int x = 0;
	while(x==0)
	{
		if(n >= 2)
		{
			n = n/2;
			e++;
			if (n < 2)
			{
				x = 1;
				return e;
			}
		}
		else return 1;
	}
	return -1;
}

float normalF(float n) /*returns frac part of normalized m*/
{
	int e = 0;
	int x = 0;
	while(x==0)
	{
		if(n >= 2)
		{
			n = n/2;
			e++;
			if (n < 2)
			{
				x = 1;
				return n-1;
			}	
		}
		else return n -1;
	}
	return -1;
}

float mBelow1(float n) /*normalizes m if below 1*/
{
	int x = 0;
	while(x==0)
	{
		if(n < 1)
		{
			n = n * 2;
			if (n >= 1)
			{
				x = 1;
				return n-1;
			}
		}
	}
	return -1;
}
int eBelow1(float n) /*used if m is below 1*/
{
	int e = 0;
	int x = 0;
	while(x==0)
	{
		if(n < 1)
		{
			n = n * 2;
			e--;
			if (n >= 1)
			{
				x = 1;
				return e;
			}
		}
	}
	return -1;
}
int getSign(fp_gmu val) /*grabs the sign bit*/
{
	if (getBit(val, 14) == 1) return 1;
	return 0;
}

int powGMU(int exp) /*returns given power of 2*/
{
	int i = 0;
	int ret = 1;
	for (i = 0; i < exp; i++)
	{
		ret = ret*2;
	}
	return ret;
}

float fracGMU(int n) /*returns fp version of m */
{
	float frac = 0; float f = .5;
	int i = 0;
	for (i = 7; i >= 0; i--)
	{
		frac += f * getBit(n, i);	
		f = f/2;
	}
	return frac;
}	
int back(float f) /*turns m back into equivalent int form for bit manipulation*/
{
	int ret = 0;
	int i = 0; 
	int j = 7;
	float one = 1; float pow = 0;
	for(i = 1; i < 8; i++)
	{
		pow = (one/powGMU(i));
		if (f - pow >= 0)
		{
			
			ret += powGMU(j);
			f -= pow;
		}
		if (f == 0) return ret;		
		j--;
	}
	return ret;
}
fp_gmu encode(int s, int exp, int mant) /*encodes into fp_gmu given s, exp, and m*/
{
	int *bits = malloc(15*sizeof(int));
	fp_gmu ret = 0;
	int i = 0;	int j = 0;
	for (i = 0; i < 15; i++) bits[i] = 0;

	while (mant > 255) /*right shifts mantissa until it is less than 255*/
	{
		mant  = mant >> 1; 
		exp++;
	}
	
	for(i = 0; i < 8; i++)
	{
		bits[i] = getBit(mant, i);
	}
	for(i = 8; i < 14; i++)
	{
		bits[i] = getBit(exp, j);
		j++;
	}
	bits[14] = s;
	for(i = 14; i >=0; i--)
	{
		if(bits[i] == 1)
		{
			bits[i] = powGMU(i);
		}
	}
	for(i = 0; i < 15; i++)
	{
		ret += bits[i];
	}
	free(bits);
	return ret;
}
float eq(int x, float m) /*makes one exp equal to the other one*/
{
	int i = 0;
	for (i = 0; i < x; i++)
	{
		m = m/2;
	}
	return m;
}

/* input: float value to be represented
 * output: fp_gmu representation of the input float
 *
 * Follow the Project Documentation for Instructions
 */
fp_gmu compute_fp(float val) {
	if (val >= 0x100000000) return 0x3F00;
	if (val <= -0x100000000) return 0x7F00;
	if (val == 0) 	return 0;
	if (val == -0) return 0x400;	
	
	int *bits = malloc(15*sizeof(int));    /*bit values are stored in an int array which is unpacked at the end into ret*/
	int e = 0;
	float f = 0;
	int bias = 31;
	int exp = 0;
	int s = 0;
	int i = 0;
	int bit = 8192;
	int pow = 32;
	float frac = .5;
	fp_gmu ret = 0;
	for (i = 0; i < 15; i++) bits[i] = 0; /*makes sure all the bits start at 0*/
	if (val < 0)
	{
		bits[0] = 16384;
		val = val * -1;
	}
	else bits[0] = 0;
	if (val < 1 && val > 0) /*computes exp and mantissa based on the given value*/
	{
		e = eBelow1(val);
		f = mBelow1(val);
	}
	else if (val > 1 && val < 2)
	{
		e = 0;
		f = normalF(val);
	}
	else if (val > 1)
	{
		e = normalE(val);
		f = normalF(val);
	}
	exp = e +bias;
	if (exp > 0)
	{
		for (i = 1; i < 7; i++) /*loop to build the exp bits*/
		{
			if(exp - pow >= 0)
			{
				exp -= pow;
				bits[i] = bit;
			}
			pow = pow/2;
			bit = bit/2;
		}	
	}
	else f = val * 0x40000000;
	if (val < 1/256) /*if the value is smaller than the smallest denormalized value, we return 0*/
	{
		free(bits);
		return 0;
	}	
	for(i = 7; i < 15; i++)        /*loop to build the frac bits */         
	{
		if(f - frac >= 0)
		{
			f -= frac;
			bits[i] = bit;
		}	
		frac = frac/2;
		bit = bit/2;	
	}

	for (i =0; i<15; i++)
	{
		ret = ret + bits[i]; /*unpacking the bits*/
	}
	
	free(bits);
	return ret;		
	
		
  /* Implement this function */
}

/* input: fp_gmu representation of our floats
 * output: float value represented by our fp_gmu encoding
 *
 * If your input fp_gmu is Infinity, return the constant INFINITY
 * If your input fp_gmu is -Infinity, return the constant -INFINITY
 * If your input fp_gmu is NaN, return the constant NAN
 *
 * Follow the Project Documentation for Instructions
 */
float get_fp(fp_gmu val) {
  /* Implement this function */
	if (val == 0x3F00) return INFINITY;
	if (val == 0x7F00) return -INFINITY;
	if ((val & 0x3F00) == 0x3F00 || (val & 0x7F00) == 0x7F00) return NAN;
	int e = 0; float m = 0; int bias = 31; float frac = 0; int exp = 0; float s = 1.0;
	int i = 0; int x = 32; float f = 0.5;
	if (getBit(val, 14) == 1) s = -1.0;
	for (i = 13; i > 7; i--)
	{
		exp += x * getBit(val, i);
		x = x/2;
	}
	if (exp == 0) e = -30;
	else e = exp - bias;
	for (i = 7; i >= 0; i--)
	{
		frac += f * getBit(val, i);	
		f = f/2;
	}
	if (exp > 0)
	{
		m = frac + 1;
		if (e >= 1)
		{
			for (i = 0; i < e; i++)
			{
				m = m*2;		/*converts the value up or down depending on exp*/
			}
		}
		else
		{
			for (i = e; i < 0; i++)
			{
				m = m/2;
			}
		}
	}
	else
	{
		m = frac * .000000000931322575; /*since exp is 0, this is a denormalized value, so we divide it by 2^-30 to reflect that*/
		return m * s;
	}
	return m * s;

}

/* input: Two fp_gmu representations of our floats
 * output: The multiplication result in our fp_gmu representation
 *
 * You must implement this using the algorithm described in class:
 *   Xor the signs: S = S1 ^ S2
 *   Add the exponents: E = E1 + E2
 *   Multiply the Frac Values: M = M1 * M2
 *   If M is not in a valid range for normalized, adjust M and E.
 *
 * Follow the Project Documentation for Instructions
 */
fp_gmu mult_vals(fp_gmu so1, fp_gmu so2) {
  /* Implement this function */
	
	if ((so1 == 0x3F00 && (so2 == 0 || so2 == 0x400)) ||  (so2 == 0x3F00 && (so1 == 0 || so1 == 0x400)))  return 0x3F01;
	if (so1 == 0x3F00 || so2 == 0x3F00) return 0x3F00;	
	if (so1 == 0x7F00 || so2 == 0x7F00) return 0x7F00;
	if ((so1 & 0x3F00) == 0x3F00 || (so1 & 0x7F00) == 0x7F00 || (so2 & 0x3F00) == 0x3F00 || (so2 & 0x7F00) == 0x7F00) return 0x3F01;
	
	if (so1 == 0 && so2 == 0) return 0;
	if (so1 == 0x400 && so2 == 0x400) return 0;	
		
	float m1 = 0;	float m2 = 0; /*mantissa1 and 2*/
	int exp1 = 0;	int exp2 = 0; int exp3 = 0; /*exp values*/
	float frac1 = 1; float frac2 = 1;/*frac values*/
	int s1 = 0;	int s2 = 0;	/*sign values*/
	int sign = 0; 			/*returning sign value*/
	int i = 0; int j = 0;		/*values for looping*/
	float prod = 0; int prod2 = 0;	/*product of mantissas, in float and int form*/
	int *bits = malloc(15*sizeof(int));
	fp_gmu ret = 0;
	for (i = 0; i < 15; i++) bits[i] = 0;
	frac1 = fracGMU(so1 & 255) + 1; 
	frac2 = fracGMU(so2 & 255) + 1;
	exp1 = getExp(so1);	exp2 = getExp(so2);
	exp3 = (exp1 + exp2) - 31;
	s1 = getSign(so1);	s2 = getSign(so2);
	sign = s1 ^ s2;
	if (so1 == 0x400 || so2 == 0x400 || so1 == 0 || so2 == 0) 
	{
		free(bits);
		if(sign) return 0x400;
		else return 0;
	}	
	prod = (frac1 * frac2);
	if (prod >= 2)
	{
		prod = normalF(prod) +1;
		exp3 +=1;	
	}
	prod2 = back(prod-1); /*product converts to int to access bits*/
	while(prod2 > 255)
	{
		prod2  = prod2 >> 1;
		exp3++;
	}
	for(i = 0; i < 8; i++)
	{
		bits[i] = getBit(prod2, i);
	}
	for(i = 8; i < 14; i++)
	{
		bits[i] = getBit(exp3, j);
		j++;
	}
	bits[14] = sign;
	for(i = 14; i >=0; i--)
	{
		if(bits[i] == 1)
		{
			bits[i] = powGMU(i); /*assigning values into bits*/
		}
	}
	for(i = 0; i < 15; i++)
	{
		ret += bits[i]; /*unpack bits unto ret*/
	}
	free(bits);
	return ret;
}

/* input: Two fp_gmu representations of our floats
 * output: The addition result in our fp_gmu representation
 *
 * You must implement this using the algorithm described in class:
 *   If needed, adjust the numbers to get the same exponent E
 *   Add the two adjusted Mantissas: M = M1 + M2
 *   Adjust M and E so that it is in the correct range for Normalized
 *
 * Follow the Project Documentation for Instructions
 */
fp_gmu add_vals(fp_gmu s1, fp_gmu s2) {
  /* Implement this function */
	float m1=0; float m2=0; float m3=0; int m4 = 0;
	int e1=0;	int e2=0;	int e3=0;
	int si1=1;	int si2=1;	int d = 0;
	int sign = 0;
	fp_gmu ret = 0;
	m1 = fracGMU(s1 & 255) + 1;
	m2 = fracGMU(s2 & 255) +1;
	e1 = getExp(s1);
	e2 = getExp(s2);
	if ((s1 == 0x3F00 && s2 == 0x7F00) ||  (s2 == 0x3F00 && s1 == 0x7F00))  return 0x3F01;	/* handles infinity and nan*/
	if (s1 == 0x3F00 || s2 == 0x3F00) return 0x3F00;
	if (s1 == 0x7F00 || s2 == 0x7F00) return 0x7F00;
	if (s1 == 0 || s1 == 0x400 && s2 != 0 && s2 != 0x400) return s2;
	if (s2 == 0 || s2 == 0x400 && s1 != 0 && s1 != 0x400) return s1;
	if ((s1 == 0 && s2 == 0) || (s1 == 0x400 && s2 == 0x400)) return 0;
	if (s1 - s2 == 0x400 || s1 - s2 == -0x400) return 0x400;
	
	si1 = getSign(s1) * -1;	si2 = getSign(s2) * -1;
	if(si1 == 0) si1 = 1; if (si2 == 0) si2 = 1;
	m1 *=si1;	m2 *= si2;
	if (e1 != e2)
	{
		if (e1 > e2)                       /*if the exp are unequal then do an operation to make them equal*/
		{
			d = e1-e2;
			m2 =eq(d, m2); 	
			e2 +=d;
			e3 = e1;
		}
		else
		{
			d = e2-e1;
			m1 =eq(d, m1);
			e1 += d;
			e3 = e2;
		}
	}
	m3 = m1 + m2;
	if (m3 < 0)                                 /*fixes mantissa for encoding*/
	{
		sign = 1;
		m3 = m3 * -1;
	}
	if(m3 > 2) 
	{
		m3 = normalF(m3) +1;		
		e3 +=1;
	}
	else if (m3 < 1 && m3 > 0) 
	{
		e3 = eBelow1(m3) + e1;
		m3 = mBelow1(m3);
	}
	if (m3 < 1) m4 = back(m3);
	else m4 = back(m3-1);
	ret = encode(sign, e3, m4);	
		
	
	return ret;

}
