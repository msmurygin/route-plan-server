package ru.ltmanagement.security.config;

//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig /* extends WebSecurityConfigurerAdapter implements WebMvcConfigurer */{

   // private InforAuthenticationFilter inforAuthenticationFilter;

    //@Autowired
    public void setAuthenticationInforFilter(InforAuthenticationFilter inforAuthenticationFilter) {
        //this.inforAuthenticationFilter = inforAuthenticationFilter;
    }

/*    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().addFilterBefore(inforAuthenticationFilter, BasicAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }*/


}
