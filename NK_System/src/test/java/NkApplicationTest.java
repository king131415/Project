/**
 *
 @RunWith(SpringRunner.class)
 @SpringBootTest  ===>测试注解



 @ContextConfiguration(classes = NkApplication.class)  ==>从那个启动类测试

 */
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ContextConfiguration(classes = NkApplication.class)
//public class NkApplicationTest implements ApplicationContextAware {
//    /**
//     * 那个类要获得Spring容器对象，就实现ApplicationContextAware接口即可
//     * 通过他的set方法即可获得容器对象。
//     *
//     * Spring容器两大API接口 :BeanFactory 和 ApplicationContext ==>子接口
//     *
//     * 核心流程:
//     *
//     * 1:启动SpringBoot的启动类后，会启动一个内嵌的服务器
//     *
//     * 2:会启动Spring容器，将含有@service、@Controll 、@commpent 、@Reposite 注解的类装配到容器中去
//     *
//     *
//     *
//     */
//
//    private  ApplicationContext applicationContext;
//    //通过set方法把容器对象注入， 首先NkApplication启动后会创建容器，
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//            this.applicationContext=applicationContext;
//    }
//
//    @Test
//    public void TestApplication(){
//        //根据类型获取实例Bean对象
//        AppDao appDao=applicationContext.getBean(AppDao.class);
//        System.out.println(appDao.select());
//
//        //通过名字获取Bean,可以在和后面加上参数的Bean类型,这样就不要强制转换
//        appDao=applicationContext.getBean("JdbcIml",AppDao.class);
//        System.out.println(appDao.select());
//    }
//
//    /**
//     * Bean的生命周期
//     */
//    @Test
//    public void TestBeanLive(){
//        AppService appService=applicationContext.getBean(AppService.class);
//        System.out.println(appService);
//
//        appService=applicationContext.getBean(AppService.class);
//        System.out.println(appService);
//
//    }
//
//    /**
//     *
//     */
//    @Test
//    public  void TestBeanConfig(){
//        SimpleDateFormat simpleDateFormat=applicationContext.getBean(SimpleDateFormat.class);
//        System.out.println(simpleDateFormat.format(new Date()));
//    }
//
//    /**
//     * 通过属性获取Bean对象，
//     * 所有的Bean对象都在容器中，所有我们可以属性注入即可(使用@Autowired)
//     */
//    @Autowired
//    @Qualifier("jdbcImlAppDao")  //消除优先级，获取指定的Bean
//    private AppDao appDao;  //使用@Autowired注解将容器中的实例Bean注册到AppDao中去
//
//    @Test
//    public void TestDI(){
//        System.out.println(appDao.select());
//    }
//
//
//
//
//
//}
