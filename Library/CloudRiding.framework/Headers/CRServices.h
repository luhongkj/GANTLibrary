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
 *支付结果回调
 *
 *@param url h5支付界面回调本APP的URL
 *
 *@param options 回调参数
 *
 *@return 处理结果（YES 已处理  NO 忽略/不处理）
 *
 */
-(BOOL)OpenURL:(NSURL*)url options:(NSDictionary *)options;


@end

NS_ASSUME_NONNULL_END
