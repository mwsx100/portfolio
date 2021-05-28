#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "fp.h"

int getBit(fp_gmu b0, int n)
{
	int bit = (b0 >> n) & 1;
	return bit;
}

float getNum(float x)
{
	if (x-1 > 0)
	{
		float y = x - (x-1);
		return y;
	}
	return 0;
}

int getExp(fp_gmu val)
{
	int i = 0; int x = 32; int exp = 0; 
	for (i = 13; i > 7; i--)
	{
		printf("%d  bit  %d\n", getBit(val,i), x);
		exp += x * getBit(val, i);
		x = x/2;
		printf("%d\n", x);
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


float getFrac(float x)
{
	int y = x;
	return x-y;
}

int normalE(float n)
{
	int e = 0;
	int x = 0;
	while(x==0)
	{
		if(n >= 2)
		{
			n = n/2;
			e++;
			printf("%d\n", e);
			if (n < 2)
			{
				printf("%d normal e \n", e);
				x = 1;
				return e;
			}
		}
		else return 1;
	}
	return -1;
}
float normalF(float n)
{
	int e = 0;
	int x = 0;
	while(x==0)
	{
		if(n >= 2)
		{
			n = n/2;
			e++;
			printf("%d\n", e);
			if (n < 2)
			{
				printf("%f normalf\n", n);
				x = 1;
				return n-1;
			}	
		}
		else return n -1;
	}
	return -1;
}

float mBelow1(float n)
{
	int x = 0;
	while(x==0)
	{
		if(n < 1)
		{
			n = n * 2;
			printf("%f\n", n);
			if (n >= 1)
			{
				printf("%f mBelow1\n", n);
				x = 1;
			//	if (n == 1) return 1;
				return n-1;
			}
		}
	}
	return -1;
}


int eBelow1(float n)
{
	int e = 0;
	int x = 0;
	while(x==0)
	{
		if(n < 1)
		{
			n = n * 2;
			e--;
			printf("%f\n", n);
			if (n >= 1)
			{
				printf("%d eBelow1\n", e);
				x = 1;
				return e;
			}
		}
	}
	return -1;
}




fp_gmu compute_fp(float val) {
	if (val >= 0x100000000) return 0x3F00;
	if (val <= -0x100000000) return 0x7F00;
	int *bits = malloc(15*sizeof(int));
	int e = 0;
	float f = 0;
	int bias = 31;
	int exp = 0;
	int s = 0;
	int i = 0;
	int bit = 8192;
	int pow = 32;
	float frac = .5;
	float dNorm = val;
	fp_gmu ret = 0;
	for (i = 0; i < 15; i++) bits[i] = 0; /*makes sure all the bits start at 0*/
	if (val == 0) return 0;
	if (val == -0) return 0x400;	
	if (val < 0)
	{
		bits[0] = 16384;
		val = val * -1;
	}
	else bits[0] = 0;
	if (val < 1 && val > 0)
	{
		printf("here\n");
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
	/*f = f-1; */
	printf("%f f\n", f);
	printf("%d e\n", e);
	exp = e +bias;
	
	printf("%d exp\n", exp);
	if (exp > 0)
	{
		for (i = 1; i < 7; i++)
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
	printf("dnorm %f\n", f);
	printf("val %f\n", val);
	if (val < 1/256)
	{
		free(bits);
		return 0;
	}	
	for(i = 7; i < 15; i++)
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
		printf("%d\n", bits[i]);
		ret = ret + bits[i];
	}
	
	printf("ret %d  %f\n", ret, f);
	free(bits);
	return ret;		
  /* Implement this function */

}

float get_fp(fp_gmu val)
{
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
	printf("%d exp\n", exp);
	if (exp == 0) e = -30;
	else e = exp - bias;
	for (i = 7; i >= 0; i--)
	{
		frac += f * getBit(val, i);	
		f = f/2;
	}
	printf("%f frac\n", f);
	printf("%f frac7\n", frac);
	if (exp > 0)
	{
		m = frac + 1;
		if (e >= 1)
		{
			for (i = 0; i < e; i++)
			{
				printf("mantissa x 2\n");
				m = m*2;
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
		m = frac * .000000000931322575;
		return m * s;
	}
	return m * s;
}

int getSign(fp_gmu val)
{
	if (getBit(val, 14) == 1) return 1;
	return 0;
}

int powGMU(int exp)
{
	int i = 0;
	int ret = 1;
	for (i = 0; i < exp; i++)
	{
		ret = ret*2;
	}
	return ret;
}

float fracGMU(int n)
{
	float frac = 0; float f = .5;
	int i = 0;
	printf("n %d\n", n);
	for (i = 7; i >= 0; i--)
	{
		frac += f * getBit(n, i);	
		f = f/2;
		printf("%d bit %f \n", getBit(n, i), f * 2);
	}
	printf("frac here %f", frac);
	return frac;
}	

int back(float f)
{
	int ret = 0;
	int i = 0; 
	int j = 7;
	float one = 1; float pow = 0;
	for(i = 1; i < 8; i++)
	{
		pow = (one/powGMU(i));
		printf("%f fpow \n", pow);
		if (f - pow >= 0)
		{
			
			ret += powGMU(j);
			f -= pow;
			printf("%f pow g\n", 1/powGMU(i));	
		}
		if (f == 0) return ret;		
		j--;
	}
	return ret;
}
fp_gmu mult_vals(fp_gmu so1, fp_gmu so2)
{
	float m1 = 0;	float m2 = 0; 
	int exp1 = 0;	int exp2 = 0; int exp3 = 0;
	float frac1 = 1; float frac2 = 1;
	int s1 = 0;	int s2 = 0;
	int sign = 0; int e = 0; float mant = 0;
	int carry = 0; int i = 0; int j = 0;
	int b1 = 0;	int b2 = 0; int c = 8;
	float prod = 0; int prod2; int soo1 = so1; int soo2 = so2; int tf = 255;
	int pos = 13; int *bits = malloc(15*sizeof(int));
	fp_gmu ret = 0;
	for (i = 0; i < 15; i++) bits[i] = 0;
	frac1 = fracGMU(so1 & 255) + 1; 
	frac2 = fracGMU(so2 & 255) + 1;
//	if (frac2 == 0) frac2 = 1; 
	exp1 = getExp(so1);	exp2 = getExp(so2);
	exp3 = (exp1 + exp2) - 31;
	s1 = getSign(so1);	s2 = getSign(so2);
	sign = s1 ^ s2;
	if (so1 == 0 && so2 == 0) return 0;
	if (so1 == 0x400 && so2 == 0x400) return 0;	
	if (so1 == 0x400 || so2 == 0x400 || so1 == 0 || so2 == 0) 
	{
		if(sign) return 0x400;
		else return 0;
	}	
	prod = (frac1 * frac2);
	if (prod >= 2)
	{
		prod = normalF(prod) +1;
		exp3 +=1;	
	}
	printf("%f prodddd\n", prod); 
	prod2 = back(prod-1);
//	if (prod == 1) prod = 0;
	printf("x1 %d x2 %d x3 %d\n", exp1, exp2, exp3);
	printf("%d %d\n", soo1, soo2);
	printf("f1 %f f2 %f prod %f\n", frac1, frac2, prod);
	while(prod2 > 255)
	{
		printf("here\n");
		prod2  = prod2 >> 1;
		exp3++;
	}
	printf("prod %d\n", prod2);	
	for(i = 0; i < 8; i++)
	{
		bits[i] = getBit(prod2, i);
		printf("%d  frac %d\n", bits[i], i);
	}
	for(i = 8; i < 14; i++)
	{
		bits[i] = getBit(exp3, j);
		j++;
		printf("%d exp bit i = %d\n", bits[i], i);
	}
	bits[14] = sign;
	for(i = 14; i >=0; i--)
	{
		if(bits[i] == 1)
		{
			bits[i] = powGMU(i);
			printf("%d pow  %d\n", bits[i], i);
		}
	}
	for(i = 0; i < 15; i++)
	{
		ret += bits[i];
	}
	

	return ret;

}
fp_gmu encode(int s, int exp, int mant)
{
	int *bits = malloc(15*sizeof(int));
	fp_gmu ret = 0;
	int i = 0;	int j = 0;
	for (i = 0; i < 15; i++) bits[i] = 0;

	while (mant > 255)
	{
		printf("here\n");
		mant  = mant >> 1;
		exp++;
	}
	
	printf("mant %d\n", mant);	
	for(i = 0; i < 8; i++)
	{
		bits[i] = getBit(mant, i);
		printf("%d  frac %d\n", bits[i], i);
	}
	for(i = 8; i < 14; i++)
	{
		bits[i] = getBit(exp, j);
		j++;
		printf("%d exp bit i = %d\n", bits[i], i);
	}
	bits[14] = s;
	for(i = 14; i >=0; i--)
	{
		if(bits[i] == 1)
		{
			bits[i] = powGMU(i);
			printf("%d pow  %d\n", bits[i], i);
		}
	}
	for(i = 0; i < 15; i++)
	{
		ret += bits[i];
	}
	free(bits);
	return ret;
}
float eq(int x, float m)
{	
	int i = 0;
	for (i = 0; i < x; i++)
	{
		m = m/2;
	}
	return m;
}

fp_gmu add_vals(fp_gmu s1, fp_gmu s2)
{
	float m1=0; float m2=0; float m3=0; int m4 = 0;
	int e1=0;	int e2=0;	int e3=0;	int e4 = 1;
	int si1=1;	int si2=1;	int d = 0;
	int sign = 0;
	fp_gmu ret = 0;
	m1 = fracGMU(s1 & 255) + 1;
	m2 = fracGMU(s2 & 255) +1;
	e1 = getExp(s1);
	e2 = getExp(s2);
	printf("e1 %d e2  %d \n", e1, e2);
	if (s1 == 0 || s1 == 0x400 && s2 != 0 && s2 != 0x400) return s2;
	if (s2 == 0 || s2 == 0x400 && s1 != 0 && s1 != 0x400) return s1;
	if ((s1 == 0 && s2 == 0) || (s1 == 0x400 && s2 == 0x400)) return 0;
	if (s1 - s2 == 0x400 || s1 - s2 == -0x400) return 0x400;
	
	si1 = getSign(s1) * -1;	si2 = getSign(s2) * -1;
	if(si1 == 0) si1 = 1; if (si2 == 0) si2 = 1;
	printf("sign: %d, m1: %f, m2: %f\n", sign, m1, m2);

	m1 *=si1;	m2 *= si2;
	printf("sign: %d, m1: %f, m2: %f\n", sign, m1, m2);

	if (e1 != e2)
	{
		if (e1 > e2)
		{
			d = e1-e2;
			m2 =eq(d, m2);	
			e2 +=d;
			e3 = e1;
			printf("here e1 > e2\n");
		}
		else
		{
			d = e2-e1;
			m1 =eq(d, m1);
			e1 += d;
			e3 = e2;
			printf("here\n");
		}
	}
	m3 = m1 + m2;
	printf("m3: %f, m1: %f, m2: %f\n", m3, m1, m2);

	if (m3 < 0)
	{
		sign = 1;
		m3 = m3 * -1;
	}
	if(m3 > 2) 
	{
		m3 = normalF(m3) +1;	
//		e4 = m3 - 2;
//		printf("e4 %d\n", e4);
		e3 += 1;	
	}
	else if (m3 < 1 && m3 > 0) 
	{
		e3 = eBelow1(m3) + e1;
		m3 = mBelow1(m3);
	}
//	e3 = e3 + e1;
	if (m3 < 1) m4 = back(m3);
	else m4 = back(m3-1);
	ret = encode(sign, e3, m4);	
		
	
	return ret;
}


int main()
{
	double small = .00000000000363797881;
//	printf("%lf s\n", small);
/*	printf("%d\n", compute_fp(-.21875));*/
//	fp_gmu x = compute_fp(-.21875);
//	printf("x ========= %d \n", x);
	//double y = floor(1000000000000*get_fp(x))/100000000000;
//	printf("%.13lf  y\n", y* 0x40000000);
	
//	compute_fp(small);
//	compute_fp(-.21875);
//	printf("%.15f small\n", small);
//	printf("%.15f\n", get_fp(compute_fp(small)));
//	printf("%.15f\n", get_fp(compute_fp(2)));
	fp_gmu x = compute_fp(1);
	fp_gmu y = compute_fp(1500000);
	printf("%.15f\n", get_fp(y));
//	printf("%.15f\n", get_fp(mult_vals(x,y)));

	return 0;
	
}
