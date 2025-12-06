import { useState, useEffect, useImperativeHandle, forwardRef } from 'react';

interface CaptchaProps {
  onCaptchaChange?: (captchaKey: string) => void;
  className?: string;
}

export interface CaptchaRef {
  refresh: () => void;
}

const Captcha = forwardRef<CaptchaRef, CaptchaProps>(({ onCaptchaChange, className = '' }, ref) => {
  const [, setCaptchaKey] = useState<string>('');
  const [imageUrl, setImageUrl] = useState<string>('');

  const generateCaptchaKey = () => {
    return Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
  };

  const refreshCaptcha = () => {
    const newKey = generateCaptchaKey();
    setCaptchaKey(newKey);
    setImageUrl(`/api/auth/captcha.jpg?key=${newKey}&t=${Date.now()}`);
    onCaptchaChange?.(newKey);
  };

  useImperativeHandle(ref, () => ({
    refresh: refreshCaptcha
  }));

  useEffect(() => {
    refreshCaptcha();
  }, []);

  const handleImageClick = () => {
    refreshCaptcha();
  };

  return (
    <div className={className} style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100%' }}>
      {imageUrl && (
        <img
          src={imageUrl}
          alt="验证码"
          width="103"
          height="40"
          style={{ 
            cursor: 'pointer',
            display: 'block'
          }}
          title="看不清可单击图片刷新"
          onClick={handleImageClick}
          onError={(e) => {
            console.error('验证码图片加载失败，URL:', imageUrl);
            e.currentTarget.alt = '验证码加载失败，点击重试';
          }}
        />
      )}
    </div>
  );
});

Captcha.displayName = 'Captcha';

export default Captcha;
