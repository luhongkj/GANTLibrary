//
//  CRServices.h
//  TestDemo1
//
//  Created by shen hansheng on 2021/5/11.
//

#import <Foundation/Foundation.h>
NS_ASSUME_NONNULL_BEGIN

@interface CRServices : NSObject


/**
 * 获取CRServices单例
 */
+ (instancetype)sharedInstancel;

/**
 APIkey。设置key，需要在高德官网控制台绑定对应的bundleid。
 */
@property (nonatomic, copy) NSString *apiKey;

/**
 当前用户token（鼎联）
 */
@property (nonatomic, copy) NSString *token;

/**
 用户手机号
 */
@property (nonatomic, copy) NSString *phoneNumber;


/**
 YES 正式环境  NO 测试环境
 */
@property (nonatomic, assign) BOOL isRelease;




/**
 *处理添加车辆方法回调
 *
 *@param addCarAtion block回调（currentVC 当前控制器对象）
 *
 *
 */
-(void)handleAddCarAtion:(void(^)(id currentVC))addCarAtion;

/**
 *支付处理回调
 *
 *@param url h5支付界面回调本APP的URL
 *
 *@param options 回调参数
 *
 *@param completion 处理结果（YES 已处理  NO 忽略/不处理）
 *
 */
-(void)OpenURL:(NSURL*)url options:(NSDictionary *)options completion:(void(^)(BOOL result))completion;


@end

NS_ASSUME_NONNULL_END
