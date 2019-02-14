#include <stdio.h>
#include <stdlib.h>
#include <string.h>

interface S{
	void send(char x);
};

interface R{
	char receive();
};

channel C
	implements S,R{
		event req;
		event ack;
		char buf;

		void send(char x){
			buf = x;
			notify req;
			wait ack;
		}

		char receive(){
			char y;
			wait req;
			y = buf;
			notify ack;
			return y;
		}
};

behavior Prod(S s){
	void main(){
		int i;
		const char *str = "Beans and Potatoes";
		i = 0;

		printf("Producer starts.\n");
		while(str[i] != '\0')
		{
			printf("Producer sends '%c'.\n",str[i]);
			s.send(str[i]);
			i++;
		}
		printf("Producer done.\n"); 
	}
};

behavior Cons(R r){
	void main(){
		const char *str;
		int l,i;
		char c;
		str = "Beans and Potatoes";
		l = strlen(str);

		printf("Consumer starts.\n");
		for(i = 0; i < l;++i)
		{
			c = r.receive();
			printf("Consumer received '%c'.\n",c);
		}
		printf("Consumer done.\n"); 
	}
};

behavior Main{
	C c;
	Prod p(c);
	Cons co(c);
  
	int main()
	{
		/* code */	
		const char *str;
     	int l;
		str = "Beans and Potatoes";
		l = strlen(str);

		printf("Main starts.\n");
		//printf("Producer starts.\n");
		//printf("Consumer starts.\n"); 
		par{
				p;co;
		}
		printf("Main done.\n");
		return 0;
	}
};