/* This is the only file you should update and submit. */

/* Fill in your Name and GNumber in the following two comment fields
 * Name: Matthew Souvannaphandhu
 * GNumber: G01187346
 */

#include "shell.h"
#include "parse.h"
//#include <unistd.h>

/* Constants */
#define DEBUG 1
#define _XOPEN_SOURCE 500
#define _XOPEN_SOURCE_EXTENDED 1
/*
 * static const char *shell_path[] = { "./", "/usr/bin/", NULL };
 * static const char *built_ins[] = { "quit", "help", "kill", 
 * "fg", "bg", "jobs", NULL};
*/
static const char R[] = "running";
static const char S[] = "stopped";
//static const char rPath[100] = "./";
//static const  char aPath[100] = "/usr/bin/";
int setpgid(pid_t pid, pid_t pgid);
pid_t getpgid(pid_t pid);
extern int errno;
typedef struct process_struct {
	char *command;
	char *state;
	int pid;
	int jid;
	Control control;
	struct process_struct *next;
	char *argv2[MAXARGS];
	int f;                           
} Process;
void insert(Process * fg); /*puts process into linked list*/
int openMe(char * in, char * out, int is_append); /*opens files for reading, incomplete*/

int fork1(char * cmd, char * const* argv, char *argv2[], int bg, sigset_t mask, Control control); /*main fork function*/
Process * grab(pid_t pid); /*gets requested pid*/
int ctrlCheck(Control control, int exit, int pid, char * cmd); /*checks control variable and exit status*/
Process * grab7(int s); /*grabs very specific background process that is the second part of an AND/OR command */

Process *p_init(char *command, int pid)
{
	Process *new;
	new = (Process*)malloc(sizeof(Process));
	new->command = malloc(sizeof(command));
	new->state = malloc(sizeof(R));
//	new->argv2 = malloc(sizeof(MAXARGS));
	strcpy(new->command, command);
	strcpy(new->state, R);
	new->pid = pid;
	new->next = NULL;
//	initialize_argv(new->argv2);
//	new->argv2 = NULL;
	return new;
}
static Process *head;
Process *fgP;
char jobs[25][MAXLINE];
int ccount = 0;
void child_handler(int sig);
void inthandler(int sig){
	log_ctrl_c();	
//	if(!strcmp(fgP->state, R))
	kill(9, fgP->pid); 
//	exit(0);
}	

//void fork2(char * cmd, char * const* argv, char * const* argv2,int bg, sigset_t mask); 

int builtIn(char *argv[]); /*checks for built in commands*/
Process * fromTheBack(pid_t pid, char * cmd); /*adds background process to job list*/

/* The entry of your shell program */
int main() {
  char cmdline[MAXLINE];        /* Command line */
  char *cmd = NULL;
//  char rPath[100] = "./";
 // char aPath[100] = "/usr/bin/";
	head = p_init("x", 0);
//	int errnum;
 // signal(SIGINT, inthandler);
  /* Intial Prompt and Welcome */
  log_prompt();
  log_help();


  /* Shell looping here to accept user command and execute */
  while (1) {
    char *argv[MAXARGS], *argv2[MAXARGS];     /* Argument list */
    Cmd_aux aux;                /* Auxilliary cmd info: check parse.h */
	
    /* Print prompt */
    log_prompt();

    /* Read a line */
    // note: fgets will keep the ending '\n'
    if (fgets(cmdline, MAXLINE, stdin) == NULL) {
      if (errno == EINTR)
        continue;
      exit(-1);
    }

    if (feof(stdin)) {  /* ctrl-d will exit shell */
      exit(0);
    }

    /* Parse command line */
    if (strlen(cmdline)==1)   /* empty cmd line will be ignored */
      continue;     

    cmdline[strlen(cmdline) - 1] = '\0';        /* remove trailing '\n' */

    cmd = malloc(strlen(cmdline) + 1);
    snprintf(cmd, strlen(cmdline) + 1, "%s", cmdline);

    /* Bail if command is only whitespace */
    if(!is_whitespace(cmd)) {
      initialize_argv(argv);    /* initialize arg lists and aux */
      initialize_argv(argv2);
      initialize_aux(&aux);
      parse(cmd, argv, argv2, &aux); /* call provided parse() */

      if (DEBUG)  /* display parse result, redefine DEBUG to turn it off */
        debug_print_parse(cmd, argv, argv2, &aux, "main (after parse)");

      /* After parsing: your code to continue from here */
      /*================================================*/


    }
//	int i = 0;
	int y = 0;
	int bg=aux.is_bg;
	Control ctrl = aux.control;
	sigset_t mask;
	sigaddset(&mask, SIGCHLD);               /*bloocks child signals for now, not sure where to put these so I just put them where ever I thought might be good*/
	sigprocmask(SIG_BLOCK, &mask, NULL);
	if (!strcmp(argv[0], "quit"))
	{ 
		log_quit();
		return 0;
	}
	else if(builtIn(argv));
	else 
	{
		if(aux.in_file != NULL || aux.out_file != NULL)
		{
		/*	if (openMe(aux.in_file, aux.out_file, aux.is_append)) not ready yet */	y = fork1(cmd, argv, argv2, bg, mask, ctrl);
 
		}
		else y = fork1(cmd, argv, argv2, bg, mask, ctrl); /*fork starts here where processes are run*/
		
	}		
	sigprocmask(SIG_UNBLOCK, &mask, NULL);  
	if(y == -7) 
	{
		Process * bgP = grab7(y);      /*-7 is just an arbitray number I chose to let me know that there is a part 2 to the process*/
		initialize_argv(bgP->argv2); /*makes a deep copy of arfv2 before it frees so I can use it later*/
		parse(cmd, argv, bgP->argv2, &aux); 		
	
	}
//	dup2(STDIN_FILENO, STDIN_FILENO);
//	dup2(STDOUT_FILENO, STDOUT_FILENO);

	free_options(&cmd, argv, argv2, &aux);
	
	}  
	return 0;
}

int openMe(char * in, char * out, int is_append)
{
	int fdin = -1;
	int fdout = -1;

	if (in != NULL && (fdin = open(in, O_WRONLY,0060)) < 0)
	{
		log_file_open_error(in);
		return -1;
		}
	else dup2(fdin, STDIN_FILENO);

	if(is_append) 
	{
		if (out != NULL && (fdout = open(out, O_APPEND, 0060)) < 0)
		{
			log_file_open_error(out);
			return -1;
		}
		else dup2(fdout, STDOUT_FILENO);

	}	
	else
	{
		if (out != NULL && (fdout = open(out, O_TRUNC, 0060)) < 0)
		{
			log_file_open_error(out);
			return -1;
		}
		else dup2(fdout, STDOUT_FILENO);

	}
	return 1;
	


}

void shandler(int sig) /*handles stop*/
{

	log_ctrl_z();
	pid_t pid = getpid();
	kill(20, pid); 

}
void child_handler(int sig) /*handles all the background process signals*/
{

	char buffer[255] = {0};
	int child_status;
	pid_t pid;
	while ((pid = waitpid(-1, &child_status, WNOHANG | WCONTINUED | WUNTRACED)) > 0) /*checks about every possible signal*/
	{	
		Process *current = head;
		Process *prev;
		while(current->next != NULL)
		{
			prev = current;
			current = current->next;
			if (current->pid == pid) break;		
		}
		if(current->pid != pid) return;	
		if(WIFCONTINUED(child_status))
		{
			strcpy(current->state, R);
			log_job_bg_cont(current->pid, current->command);	
			return;
		}
		if(WIFSTOPPED(child_status))
		{
			strcpy(current->state, S);
			log_job_bg_stopped(current->pid, current->command);
			return;	
		}
		if(WIFEXITED(child_status)) 
		{
			int d = 1; /*d is for done*/
			if(current->f == -7)
			{
				int exit =WEXITSTATUS(child_status);
				if(current->control == AND && exit == 0)
				{
					sigset_t mask;
					int pid2 = fork1(current->command, NULL, current->argv2, 1, mask, NONE);
					if(pid2 != 0) 
					{
						log_and_list(pid, pid2, current->command);
						free_argv(current->argv2);
						d = 0;
					}
				}
				else if(current->control == OR && exit != 0)
				{
					sigset_t mask;
					int pid2 = fork1(current->command, NULL, current->argv2, 1, mask, NONE);
					if(pid2 != 0)
					{
						log_or_list(pid, pid2, current->command);
						free_argv(current->argv2);
						d = 0;
					}
				}
				else
				{	
					if(current->control == AND) log_and_list(pid, -1, current->command);
					else log_or_list(pid, -1, current->command);
				}
				 			
			} 
			if(d) log_job_bg_term(current->pid, current->command);
		}
		else if(WIFSIGNALED(child_status))
		{
			log_job_bg_term_sig(current->pid, current->command);
		}
		prev->next = current->next;
		free(current);
		ccount--;
		return;
	}
//	sprintf(buffer, "WNOIHANG %s\n", strerror(errno));
//	write(STDOUT_FILENO, buffer, strlen(buffer));
}


int fork1(char * cmd, char * const* argv, char *argv2[], int bg, sigset_t mask, Control control)
{
	int r = 0;
	char rPath[100] = "./";
 	char aPath[100] = "/usr/bin/";
	struct sigaction act;
	memset(&act, 0, sizeof(struct sigaction)); /*handlers set up here*/
	act.sa_handler = child_handler;
	sigaction(SIGCHLD, &act, NULL); 
	struct sigaction act2;
	memset(&act2, 0, sizeof(struct sigaction));
	act2.sa_handler = inthandler;
	sigaction(SIGINT, &act2, NULL);
	struct sigaction act3;
	memset(&act3, 0, sizeof(struct sigaction));
	act3.sa_handler = shandler;
	sigaction(SIGTSTP, &act3, NULL);

	int child_status;
	if(argv == NULL) /*if argv is null then this is recursive*/
	{
		r = 1;
		argv = argv2; /*when recursive, use argv2 instead*/
		
	}
	pid_t pid = fork(); /*fork starts here*/
	
	if(pid == 0)  /*child process*/
	{	                  /*execv stuff happens here*/
		if ((execv(strcat(rPath, argv[0]), argv)) != -1  || (execv(strcat(aPath, argv[0]), argv)) != -1 ); 
		else if((execl(strcat(rPath, argv[0]), *argv)) != -1  || (execl(strcat(aPath, argv[0]), *argv)) != -1 ); 
		else log_command_error(cmd);
		exit(0);
	}
	else 
	{               /*parent process*/
		if(!bg) /*foreground*/
		{
			if (r) return pid;	/*if recursive we don't need to be here anymore*/
			log_start_fg(pid, cmd); 
			fgP = p_init(cmd, pid); /*foreground process  saved globally*/
			while ((pid = waitpid(pid, &child_status, WUNTRACED)) == -1) /*waits for foreground to terminate*/
			{
				sigprocmask(SIG_BLOCK, &mask, NULL); /*meant to block child signals while it waits*/

			//	printf("%s \n", strerror(errno));
			}
			if(WIFEXITED(child_status))  /*if exited*/
			{
				if (ctrlCheck(control, WEXITSTATUS(child_status), pid, cmd)) /*if control == AND/OR and exitstatus == 0/1 */
				{
					pid_t pid2 = fork1(cmd, NULL, argv2, bg, mask, NONE);   /*does part 2 of foreground AND/OR, recursive call to fork1*/
					if(control == AND) log_and_list(pid, pid2, cmd); /*prints out appropriate log message*/
					else log_or_list(pid, pid2, cmd);
					while ((pid2 = waitpid(pid2, &child_status, 0)) == -1);	 /*waits for AND/OR to be done*/
				}
		//		free(fgP);
				if(WIFSIGNALED(child_status)) /*if terminated by a signal*/
				{
					log_job_fg_term_sig(pid, cmd);
				}
				else log_job_fg_term(pid,cmd);
	
			}
			else if(WIFSTOPPED(child_status)) /*if process is stopped*/
			{
				log_job_fg_stopped(pid, cmd);
				insert(fgP);                     /*puts foreground process into background linked list*/
				strcpy(fgP->state, S);            /*switch fgP state to stopped*/  

				ccount++;	                   /*increment job count*/
				sigprocmask(SIG_UNBLOCK, &mask, NULL);  /*unblocks child signals*/

			}
		}
		else
		{                                                   /*backGground*/
			sigprocmask(SIG_UNBLOCK, &mask, NULL);  /*unblock child processes*/
	
			if (!r) log_start_bg(pid,cmd);  /*starts background log if its not recursive*/
			if(setpgid(pid, 0) != 0);  /*changes gid so it doesn't follow the parent*/

			fromTheBack(pid, cmd); /*inserts job*/ 
			
			if(control != NONE) /*aka its AND/OR*/
			{
				Process *bgP = grab(pid); /*grabs process matching pid from the list */
				bgP->control = control;  /*sets the control flag to AND/OR */
				bgP->f = -7;             /*sets it to -7 which lets us know that it's a 2 part thing*/
				return -7;
			}
		}
		return pid;
	}
	return -1;
}


void insert(Process * fg)
{
	Process * current = head;
	while(current->next != NULL)
	{
	//	printf("%d\n", current->jid);
		current=  current->next;
	//	if(current->f == s) return current;
	}
	fg->jid = current->jid+1;	
	current->next = fg;                          
	int gid = getpgid(current->pid);
	if (setpgid(fg->pid, gid)!=0); // this fails im not sure  how to fix it	printf("%s insert\n", strerror(errno));

	
}

Process * grab7(int s)
{                                          /* grabs the process where f = 7*/
	Process * current = head;
	while(current->next != NULL)
	{
	//	printf("%d\n", current->jid);
		current=  current->next;
		if(current->f == s) return current;
	}
	return NULL;

}
int ctrlCheck(Control control, int exit, int pid, char * cmd)
{
	if(control == NONE) return 0;
	else if (control == AND)
	{
		if(exit == 0) return 1;
		else
		{
			 log_and_list(pid, -1, cmd);
			 return 0;
		}
	}
	else if (control == OR)                         
	{
		if(exit != 0) return 1;
		else
		{
			log_or_list(pid, -1, cmd);
			return 0;
		}
	}
	else return 0; 
}
Process * grab(pid_t pid)
{
	Process * current = head;
	while(current->next != NULL)
	{
	//	printf("%d\n", current->jid);
		current=  current->next;
		if(current->pid == pid) return current;
	}
	return NULL;
}

Process * fromTheBack(pid_t pid, char * cmd) /*know u like it like that*/
{
	Process *bgP = p_init(cmd, pid);
	ccount++;
	if(head->next == NULL) 
	{
		head->next =bgP;
		bgP->jid = 1;
		return bgP;
	}
	else
	{
		Process * current = head;
		while(current->next != NULL)
		{
		//	printf("%d\n", current->jid);
			current=  current->next;
			if(current->next == NULL)
			{
				current->next = bgP;
				current->next->jid = current->jid+1;
				break;
			}
		}
		return bgP;					
	}
}

void free_options2(char **cmd, char *argv[], Cmd_aux *aux) { /*unused*/
  if(*cmd) {
    free(*cmd);
    *cmd = NULL;
  }

  free_argv(argv);
//  free_argv(argv2);

  if(aux->in_file) {
    free(aux->in_file);
    aux->in_file = NULL;
  }
  if(aux->out_file) {
    free(aux->out_file);
    aux->out_file = NULL;
  }
}

int builtIn(char *argv[])
{	
		if (!strcmp(argv[0], "help"))
		{
			log_help();
			return 1;
		}
		else if (!strcmp(argv[0], "jobs"))
		{
			Process * current= head;
			int num_jobs = ccount;
			log_job_number(num_jobs);	
			while(current->next != NULL)
			{
				current = current->next;
				log_job_details(current->jid, current->pid, current->state, current->command);
			}
			return 1;
			
		}
		else if (!strcmp(argv[0], "kill"))
		{
			char c = argv[1][0];
			int id = atoi(argv[2]);
			pid_t Kpid = id;
			switch(c)
			{
				case '2' :
					if(argv[1][1] == '0') 
					{
						kill(Kpid, 20);
						log_kill(SIGTSTP, Kpid);
					}
					else 
					{
						kill(Kpid, 2);
						log_kill(SIGINT, Kpid);
					}	
					break;
				case '9':
					kill(Kpid, 9);
					log_kill(SIGKILL, Kpid);
					break;
				case '1':
					kill(Kpid, 18);
					log_kill(SIGCONT, Kpid);
					break;
				default: break;
					
				
			}			
			return 1;
			
		}
		else if (!strcmp(argv[0], "fg"))
		{
			int fjid = atoi(argv[1]);	
			Process *current = head;
			Process * prev;
			int child_status;
			while (current->next != NULL)
			{
				prev = current;
				current = current->next;
				if(current->jid == fjid)
				{
					if(current->next != NULL) prev->next = current->next;	
					else prev->next = NULL;
					break;
				}
			}
			if(current->jid != fjid) log_jobid_error(fjid);
			else
			{
				if(!strcmp(current->state, S))
				{
					strcpy(current->state, R);
					log_job_bg_cont(current->pid,current->command);
					kill(current->pid, 18);
				}
				pid_t pid = current->pid;
				fgP = current;
				log_job_fg(pid, current->command);
				while ((pid = waitpid(pid, &child_status, 0)) == -1);
				log_job_fg_term(pid,current->command);
				ccount--;
				free(current);
			}
			return 1;
		}
		else if (!strcmp(argv[0], "bg"))
		{
			int bjid = atoi(argv[1]);	
			Process *current = head;
	//		int child_status;
			while (current->next != NULL)
			{
				current = current->next;
				if(current->jid == bjid) break;
				
			}
			if(current->jid != bjid) log_jobid_error(bjid);
			else if(!strcmp(current->state, R)) log_jobid_error(bjid);
  //printf("Job ID %d already running.\n", bjid);
			else
			{
				kill(current->pid, 18);
			}
			return 1;
		}
	
		else return 0;
	


}
